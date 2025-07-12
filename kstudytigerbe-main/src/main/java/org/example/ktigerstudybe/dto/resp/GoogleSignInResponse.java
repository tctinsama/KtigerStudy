package org.example.ktigerstudybe.dto.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleSignInResponse {
    private Long userId;
    private String email;
    private String fullName;
    private String role;
    private boolean isNewUser;
    private String message;
}
