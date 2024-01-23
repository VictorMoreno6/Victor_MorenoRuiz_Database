package dao.impl;

import common.Constants;
import dao.CustomersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Customer;
import model.errors.CustomerError;
import model.hibernate.CredentialsEntity;
import model.hibernate.CustomersEntity;
import model.hibernate.OrdersEntity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDaoHibernate implements CustomersDAO {


    private final JPAUtil jpautil;

    private EntityManager entityManager;

    @Inject
    public CustomerDaoHibernate(DBConnection db, JPAUtil jpautil) {
        this.jpautil = jpautil;
    }

    @Override
    public Either<CustomerError, List<Customer>> getAll() {
        List<CustomersEntity> list;
        List<Customer> customerList = new ArrayList<>();
        try {
            entityManager = jpautil.getEntityManager();
            list = entityManager.createQuery("from CustomersEntity", CustomersEntity.class).getResultList();
            if (list.isEmpty()) {
                return Either.left(new CustomerError(0, Constants.ERROR_CONNECTING_TO_DATABASE));
            } else {
                for (CustomersEntity entity : list) {
                    Customer customer = new Customer();
                    customer.setId(entity.getId());
                    customer.setFirst_name(entity.getFirst_name());
                    customer.setLast_name(entity.getLast_name());
                    customer.setEmail(entity.getEmail());
                    customer.setPhone(entity.getPhone());
                    customer.setDob(entity.getDob().toLocalDate());

                    customerList.add(customer);
                }
                return Either.right(customerList);
            }
        } finally {
            if (this.entityManager != null) {
                this.entityManager.close();
            }
        }
    }

    @Override
    public Either<CustomerError, Customer> get(int id) {
        try {
            entityManager = jpautil.getEntityManager();
            CustomersEntity customerEntity = entityManager.find(CustomersEntity.class, id);

            if (customerEntity == null) {
                return Either.left(new CustomerError(0, Constants.ERROR_CONNECTING_TO_DATABASE));
            } else {
                Customer customer = new Customer();
                customer.setId(customerEntity.getId());
                customer.setFirst_name(customerEntity.getFirst_name());
                customer.setLast_name(customerEntity.getLast_name());
                customer.setEmail(customerEntity.getEmail());
                customer.setPhone(customerEntity.getPhone());
                customer.setDob(customerEntity.getDob().toLocalDate());

                return Either.right(customer);
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public Either<CustomerError, Integer> save(Customer c) {
        Either<CustomerError, Integer> either;

        EntityTransaction tx = null;

        try {
            entityManager = jpautil.getEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();


            CredentialsEntity credential = new CredentialsEntity();
            credential.setUsername(c.getCredential().getUsername());
            credential.setPassword(c.getCredential().getPassword());

            entityManager.persist(credential);

            int credentialId = credential.getId();

            CustomersEntity customerEntity = new CustomersEntity();
            customerEntity.setId(credentialId);
            customerEntity.setFirst_name(c.getFirst_name());
            customerEntity.setLast_name(c.getLast_name());
            customerEntity.setEmail(c.getEmail());
            customerEntity.setPhone(c.getPhone());
            customerEntity.setDob(Date.valueOf(c.getDob()));

            entityManager.persist(customerEntity);

            tx.commit();
            either = Either.right(0);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            Logger.getLogger(CustomerDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
            either = Either.left(new CustomerError(0, "Error connecting database"));
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return either;
    }

    @Override
    public Either<CustomerError, Integer> update(Customer customer) {
        Either<CustomerError, Integer> either;

        try {
            entityManager = jpautil.getEntityManager();
            entityManager.getTransaction().begin();

            CustomersEntity customerEntity = entityManager.find(CustomersEntity.class, customer.getId());

            if (customerEntity == null) {
                either = Either.left(new CustomerError(0, "Customer not found"));
            } else {
                // Update the fields
                customerEntity.setFirst_name(customer.getFirst_name());
                customerEntity.setLast_name(customer.getLast_name());
                customerEntity.setEmail(customer.getEmail());
                customerEntity.setPhone(customer.getPhone());
                customerEntity.setDob(Date.valueOf(customer.getDob()));

                entityManager.getTransaction().commit();
                either = Either.right(1);
            }
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            Logger.getLogger(CustomerDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
            either = Either.left(new CustomerError(0, "Error connecting database"));
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return either;
    }

    @Override
    public Either<CustomerError, Integer> delete(Customer customer, boolean deleteOrders) {
        Either<CustomerError, Integer> result;

        try {
            entityManager = jpautil.getEntityManager();
            entityManager.getTransaction().begin();

            CustomersEntity customerEntity = entityManager.find(CustomersEntity.class, customer.getId());
            if (deleteOrders) {
                entityManager.createQuery("DELETE FROM OrderItemsEntity oi WHERE oi.idOrder IN (SELECT o.id FROM OrdersEntity o WHERE o.customer_id = :customerId)")
                        .setParameter("customerId", customer.getId())
                        .executeUpdate();
                entityManager.createQuery("DELETE FROM OrdersEntity o WHERE o.customer_id = :customerId")
                        .setParameter("customerId", customer.getId())
                        .executeUpdate();

                entityManager.remove(customerEntity);
                CredentialsEntity credentialsEntity = entityManager.find(CredentialsEntity.class, customer.getId());
                if (credentialsEntity != null) {
                    entityManager.remove(credentialsEntity);
                }
            } else {
                OrdersEntity ordersEntity = entityManager.find(OrdersEntity.class, customer.getId());
                if (ordersEntity != null) {
                    return Either.left(new CustomerError(2, "The customer has orders"));
                }
                entityManager.remove(customerEntity);
                CredentialsEntity credentialsEntity = entityManager.find(CredentialsEntity.class, customer.getId());
                if (credentialsEntity != null) {
                    entityManager.remove(credentialsEntity);
                }
            }
            entityManager.getTransaction().commit();
            result = Either.right(0);

        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            Logger.getLogger(CustomerDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
            result = Either.left(new CustomerError(1, "Error connecting to the database"));
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return result;
    }


}
