package model.hibernate;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Collection;
@Data
@Entity
@Table(name = "orders", schema = "victormoreno_restaurant", catalog = "")
public class OrdersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    private int id;
    @Column(name = "order_date", nullable = false)
    private Timestamp date;
    @Column(name = "customer_id", nullable = false,  insertable = false, updatable = false)
    private int customer_id;
    @Column(name = "table_id", nullable = false)
    private int table_id;
    @OneToMany(mappedBy = "ordersByOrderId")
    private Collection<OrderItemsEntity> orderItemsByOrderId;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = true)
    private CustomersEntity customersByCustomerId;

//    @ManyToOne
//    @JoinColumn(name = "table_id", referencedColumnName = "table_number_id")
//    private RestaurantTablesEntity restaurantTablesByTableId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdersEntity that = (OrdersEntity) o;

        if (id != that.id) return false;
        if (customer_id != that.customer_id) return false;
        if (table_id != that.table_id) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + customer_id;
        result = 31 * result + table_id;
        return result;
    }

    public Collection<OrderItemsEntity> getOrderItemsByOrderId() {
        return orderItemsByOrderId;
    }

    public void setOrderItemsByOrderId(Collection<OrderItemsEntity> orderItemsByOrderId) {
        this.orderItemsByOrderId = orderItemsByOrderId;
    }

    public CustomersEntity getCustomersByCustomerId() {
        return customersByCustomerId;
    }

    public void setCustomersByCustomerId(CustomersEntity customersByCustomerId) {
        this.customersByCustomerId = customersByCustomerId;
    }
}
