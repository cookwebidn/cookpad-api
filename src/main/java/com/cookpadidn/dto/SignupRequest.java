package com.cookpadidn.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SignupRequest {

    @NotEmpty(message = "name is required")
    private String name;

    @NotEmpty(message = "email address is required")
    @Email(message = "should be valid email")
    private String email;

    @NotEmpty(message = "password is required")
    @Size(min = 8, max = 20, message ="password length must be minimum 8 and maximum 20 character")
    private String password;

    @JsonIgnore
    private List<String> roles;
}
