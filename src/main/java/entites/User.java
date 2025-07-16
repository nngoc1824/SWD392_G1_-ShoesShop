package entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String googleId;
    private List<String> roles;
}
