package model.hibernate;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
@Data
@Entity
@Table(name = "restaurant_tables", schema = "victormoreno_restaurant")
public class RestaurantTablesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "table_number_id", nullable = false)
    private int id;
    @Column(name = "number_of_seats", nullable = false)
    private int seats;
//    @OneToMany(mappedBy = "restaurantTablesByTableId")
//    private Collection<OrdersEntity> ordersByTableNumberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestaurantTablesEntity that = (RestaurantTablesEntity) o;

        if (id != that.id) return false;
        if (seats != that.seats) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + id;
        return result;
    }

//    public Collection<OrdersEntity> getOrdersByTableNumberId() {
//        return ordersByTableNumberId;
//    }
//
//    public void setOrdersByTableNumberId(Collection<OrdersEntity> ordersByTableNumberId) {
//        this.ordersByTableNumberId = ordersByTableNumberId;
//    }
}
