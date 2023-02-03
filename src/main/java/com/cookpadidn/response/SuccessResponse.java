package com.cookpadidn.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@JsonPropertyOrder({
        "success",
        "message"
})
@Builder
@AllArgsConstructor
public class SuccessResponse {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    @JsonIgnore
    private HttpStatus httpStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private transient Object data;

    public SuccessResponse() {
    }
}
