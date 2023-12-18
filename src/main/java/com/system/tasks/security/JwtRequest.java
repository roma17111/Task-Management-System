package com.system.tasks.security;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    private String login;
    private String password;
}
