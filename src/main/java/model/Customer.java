package model;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder @ToString
public class Customer {
    @BsonId
    private ObjectId _id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    @SerializedName("date_of_birth")
    private LocalDate dob;
    List<Order> orders;



    public Customer(String fileline){
        String[] elemArray = fileline.split(";");
        this._id= new ObjectId(elemArray[0]);
        this.first_name= elemArray[1];
        this.last_name= elemArray[2];
        this.email= elemArray[3];
        this.phone= elemArray[4];
        this.dob= LocalDate.parse(elemArray[5]);
    }


    public String toStringTextfile() {
        return  _id + ";" +
                first_name + ";"+
                last_name + ";" +
                email + ";" +
                phone + ";" +
                dob ;
    }

    public Customer(ObjectId id, String first_name, String last_name, String email, String phone, LocalDate dob) {
        this._id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
    }

    public Customer(String first_name, String last_name, String email, String phone, LocalDate dob) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
    }
}
