package entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    private int userId;
    private String userName;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String image;
    private int status;
    private int addressId;
    private String googleId;
}
