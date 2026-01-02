package com.springBoot.Oauth2Example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListResponse {

    private Boolean success;
    private HttpStatus statusCode;
    private List<?> data;
}
