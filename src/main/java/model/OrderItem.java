package model;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.SecondaryTable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder @ToString
public class OrderItem {
    @SerializedName("menu_item_id")
    private int menuItemId;
    private int quantity;

}
