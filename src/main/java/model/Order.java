package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
public class Order {

    private int id;
    private LocalDateTime date;
    private int customer_id;
    private int table_id;
    private List<OrderItem> orderItems;

    public Order(String fileline){
        String[] elemArray = fileline.split(";");
        this.id= Integer.parseInt(elemArray[0]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = LocalDateTime.parse(elemArray[1], formatter);
        this.customer_id= Integer.parseInt(elemArray[2]);
        this.table_id= Integer.parseInt(elemArray[3]);
    }

    public String toStringTextfile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = date.format(formatter);
        return  id + ";" +
                formattedDateTime + ";"+
                customer_id + ";" +
                table_id;
    }

    public Order() {
        //
    }

    public Order(int id, LocalDateTime date, int customer_id, int table_id) {
        this.id = id;
        this.date = date;
        this.customer_id = customer_id;
        this.table_id = table_id;
    }
}
