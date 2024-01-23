package model.hibernate;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items", schema = "victormoreno_restaurant")
public class OrderItemsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_item_id", nullable = false)
    private int id;
    @Column(name = "order_id",  insertable = false, updatable = false)
    private int idOrder;
    @Column(name = "menu_item_id", insertable = false, updatable = false)
    private int menuItem;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "menu_item_id", referencedColumnName = "menu_item_id", nullable = false)
    private MenuItemsEntity menuItemsByMenuItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private OrdersEntity ordersByOrderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItemsEntity that = (OrderItemsEntity) o;

        if (id != that.id) return false;
        if (idOrder != that.idOrder) return false;
        if (menuItem != that.menuItem) return false;
        if (quantity != that.quantity) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + idOrder;
        result = 31 * result + menuItem;
        result = 31 * result + quantity;
        return result;
    }

    public MenuItemsEntity getMenuItemsByMenuItemId() {
        return menuItemsByMenuItemId;
    }

    public void setMenuItemsByMenuItemId(MenuItemsEntity menuItemsByMenuItemId) {
        this.menuItemsByMenuItemId = menuItemsByMenuItemId;
    }
}
