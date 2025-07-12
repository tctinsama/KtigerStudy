package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class ClassRequest {
    private String className;
    private String description;
    private Long userId;
    private String password;
}
