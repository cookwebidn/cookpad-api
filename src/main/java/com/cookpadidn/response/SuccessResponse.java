package com.cookpadidn.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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

    public SuccessResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public SuccessResponse(Boolean success, String message, Object object) {
        this.success = success;
        this.message = message;
        this.data = object;
    }

    public SuccessResponse(Boolean success, String message, HttpStatus httpStatus) {
        this.success = success;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
