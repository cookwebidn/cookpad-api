package com.cookpadidn.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
public class UserRequest {

    @JsonIgnore
    private String id;

    private String name;

    @Email(message = "should be valid email")
    private String email;

    @NotEmpty(message = "password is required for verification")
    private String password;

    @JsonIgnore
    private List<String> role;
}
