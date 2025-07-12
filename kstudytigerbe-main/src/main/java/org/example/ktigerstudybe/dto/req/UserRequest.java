package org.example.ktigerstudybe.dto.req;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRequest {
    private String fullName;
    private String email;
    private String password;
    private String gender;
    private LocalDate dateOfBirth;
    private String avatarImage;
    private LocalDate joinDate;
    private String role;
    private int userStatus;
    private String userName;
}
