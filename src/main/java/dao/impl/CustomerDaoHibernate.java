package dao.impl;

import common.Constants;
import dao.CustomersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Credential;
import model.Customer;
import model.MenuItem;
import model.Order;
import model.errors.CustomerError;
import model.hibernate.CredentialsEntity;
import model.hibernate.CustomersEntity;
import model.hibernate.MenuItemsEntity;
import model.hibernate.OrdersEntity;
import org.bson.types.ObjectId;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

//TODO
//Se supone que solo tiene que tener getAllCustomers y getMenuItems
@Named("customerDaoHibernate")
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
//                    customer.set_id(entity.getId());
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
    public Either<CustomerError, Customer> get(ObjectId id) {
        return null;
    }

//    @Override
//    public Either<CustomerError, Customer> get(int id) {
//        return null;
//    }

    @Override
    public Either<CustomerError, Integer> save(Customer c) {
        return null;
    }

    @Override
    public Either<CustomerError, Integer> save(Customer c, Credential credential) {
        return null;
    }


    @Override
    public Either<CustomerError, Integer> update(Customer customer) {
        return null;
    }

    @Override
    public Either<CustomerError, Integer> delete(Customer c, boolean deleteOrders) {
        return null;
    }

    @Override
    public Customer getCustomerByOrder(Order order) {
        return null;
    }




    //TODO
    //Aqui esta el menuItem con hibernate
//    @Override
//    public Either<String, List<MenuItem>> getAll() {
//        Either<String, List<MenuItem>> result = null;
//
//        try {
//            entityManager = jpautil.getEntityManager();
//
//            TypedQuery<MenuItemsEntity> query = entityManager.createQuery("SELECT mi FROM MenuItemsEntity mi", MenuItemsEntity.class);
//            List<MenuItemsEntity> menuItemsEntities = query.getResultList();
//
//            List<MenuItem> menuItems = menuItemsEntities.stream()
//                    .map(menuItemsEntity -> new MenuItem(
//                            menuItemsEntity.getId(),
//                            menuItemsEntity.getName(),
//                            menuItemsEntity.getDescription(),
//                            menuItemsEntity.getPrice()
//                    ))
//                    .collect(Collectors.toList());
//
//            result = Either.right(menuItems);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            result = Either.left(Constants.ERROR_CONNECTING_TO_DATABASE);
//        } finally {
//            if (entityManager != null) {
//                entityManager.close();
//            }
//        }
//
//        return result;
//    }
//
//    @Override
//    public Either<String, MenuItem> get(int id) {
//        Either<String, MenuItem> result = null;
//
//        try {
//            entityManager = jpautil.getEntityManager();
//
//            MenuItemsEntity menuItemsEntity = entityManager.find(MenuItemsEntity.class, id);
//
//            if (menuItemsEntity == null) {
//                result = Either.left("No menu item found");
//            } else {
//                MenuItem menuItem = new MenuItem(
//                        menuItemsEntity.getId(),
//                        menuItemsEntity.getName(),
//                        menuItemsEntity.getDescription(),
//                        menuItemsEntity.getPrice()
//                );
//                result = Either.right(menuItem);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            result = Either.left(Constants.ERROR_CONNECTING_TO_DATABASE);
//        } finally {
//            if (entityManager != null) {
//                entityManager.close();
//            }
//        }
//
//        return result;
//    }

}
