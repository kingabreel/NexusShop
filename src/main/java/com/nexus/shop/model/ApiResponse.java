package com.nexus.shop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    @Getter
    @Setter
    private T data;
    @Getter
    @Setter
    private String message;
    
}
