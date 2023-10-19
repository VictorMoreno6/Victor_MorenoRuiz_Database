package model;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class OrderItem {

    private int id;
    private int idOrder;
    private model.MenuItem menuItem;
    private int quantity;

}
