package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
