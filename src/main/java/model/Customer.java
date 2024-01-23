package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Customer {
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private LocalDate dob;
    private Credential credential;



    public Customer(String fileline){
        String[] elemArray = fileline.split(";");
        this.id= Integer.parseInt(elemArray[0]);
        this.first_name= elemArray[1];
        this.last_name= elemArray[2];
        this.email= elemArray[3];
        this.phone= elemArray[4];
        this.dob= LocalDate.parse(elemArray[5]);
    }

    public Customer() {

    }

    public String toStringTextfile() {
        return  id + ";" +
                first_name + ";"+
                last_name + ";" +
                email + ";" +
                phone + ";" +
                dob ;
    }

    public Customer(String first_name, String last_name, String email, String phone, LocalDate dob, Credential credential) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.credential = credential;
    }

    public Customer(int id, String first_name, String last_name, String email, String phone, LocalDate dob) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
    }
}
