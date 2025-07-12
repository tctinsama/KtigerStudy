package org.example.ktigerstudybe.dto.resp;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String gender;
    private LocalDate dateOfBirth;
    private String avatarImage;
    private LocalDate joinDate;
    private String role;
    private int userStatus;
    private String userName;
}
