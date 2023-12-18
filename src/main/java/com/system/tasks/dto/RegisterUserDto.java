package com.system.tasks.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class RegisterUserDto {

    /**
     * Dto для регистрации полльзователя
     */

    String firstName;
    String lastName;
    String secondName;
    String email;
    String password;
}
