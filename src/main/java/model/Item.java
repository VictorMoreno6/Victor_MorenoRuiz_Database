package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private int id;
    private String name;
    private String description;
    private float price;
}
