package model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
@XmlRootElement(name="orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ordersxml {

    @XmlElement(name="order")
    List<Orderxml> orders;

    public Ordersxml() {
    }
}
