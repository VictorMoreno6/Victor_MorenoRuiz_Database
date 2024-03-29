package model.hibernate;

import jakarta.persistence.*;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Entity
@Table(name = "credentials", schema = "victormoreno_restaurant")
public class CredentialsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "_id", nullable = false)
    private ObjectId id;

    @Column(name = "user_name", nullable = false, length = 20)
    private String username;

    @Column(name = "password", nullable = false, length = 20)
    private String password;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CredentialsEntity that = (CredentialsEntity) o;

        if (id != that.id) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

}
