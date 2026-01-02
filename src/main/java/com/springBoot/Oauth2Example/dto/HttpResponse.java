package com.springBoot.Oauth2Example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {

    private Boolean success;
    private HttpStatus successCode;
    private Integer pageNumber;       // Current page number (0-based or 1-based)
    private Integer pageSize;         // Number of items per page
    private Integer totalElements;   // Total number of items across all pages
    private Integer totalPages;       // Total pages available
    private Boolean last;
    private List<?> data;      // List of records on current page

}
