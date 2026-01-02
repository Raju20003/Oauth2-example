package com.springBoot.Oauth2Example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class DataResponse {

    private Boolean success;
    private HttpStatus statusCode;
    private Object data;

}
