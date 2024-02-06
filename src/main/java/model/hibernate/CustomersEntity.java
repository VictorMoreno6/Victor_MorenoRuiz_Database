package model.hibernate;

import jakarta.persistence.*;
import lombok.Data;
import org.bson.types.ObjectId;

import java.sql.Date;
@Data
@Entity
@Table(name = "customers", schema = "victormoreno_restaurant")
public class CustomersEntity {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "first_name", nullable = false, length = 20)
    private String first_name;
    @Column(name = "last_name", nullable = false, length = 20)
    private String last_name;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "phone", nullable = true, length = 20)
    private String phone;
    @Column(name = "date_of_birth", nullable = true)
    private Date dob;
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private CredentialsEntity credentialsById;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomersEntity that = (CustomersEntity) o;

        if (id != that.id) return false;
        if (first_name != null ? !first_name.equals(that.first_name) : that.first_name != null) return false;
        if (last_name != null ? !last_name.equals(that.last_name) : that.last_name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (dob != null ? !dob.equals(that.dob) : that.dob != null) return false;

        return true;
    }



    public CredentialsEntity getCredentialsById() {
        return credentialsById;
    }

    public void setCredentialsById(CredentialsEntity credentialsById) {
        this.credentialsById = credentialsById;
    }
}
