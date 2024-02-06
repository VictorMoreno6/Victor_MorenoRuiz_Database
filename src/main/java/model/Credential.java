package model;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder @ToString
public class Credential {
    @BsonId
    @Getter(AccessLevel.PUBLIC)
    public ObjectId _id;
    @Getter(AccessLevel.PUBLIC)
    @SerializedName("user_name")
    private String username;
    @Getter(AccessLevel.PUBLIC)
    private String password;

    public Credential(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
