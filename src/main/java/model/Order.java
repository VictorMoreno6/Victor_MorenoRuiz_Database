package model;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder @ToString
public class Order {

    @SerializedName("order_date")
    private LocalDateTime date;
    private int table_id;
    @SerializedName("order_items")
    private List<OrderItem> orderItems;

    public Order(String fileline){
        String[] elemArray = fileline.split(";");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = LocalDateTime.parse(elemArray[1], formatter);
        this.table_id= Integer.parseInt(elemArray[2]);
    }

    public String toStringTextfile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = date.format(formatter);
        return formattedDateTime + ";"+
                table_id;
    }


    public Order( LocalDateTime date, int table_id) {
        this.date = date;
        this.table_id = table_id;
    }
}
