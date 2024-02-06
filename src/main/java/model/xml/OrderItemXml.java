package model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@XmlRootElement(name="order_item")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItemXml {

    @XmlElement(name="menu_item")
//    private String menuItem;
    private int menuItem;

    @XmlElement(name="quantity")
    private int quantity;

    public OrderItemXml() {
    }
}
