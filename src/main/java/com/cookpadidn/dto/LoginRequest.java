package com.cookpadidn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class LoginRequest {

    @NotNull @NotBlank
    private String email;

    @NotNull @NotBlank
    private String password;
}
