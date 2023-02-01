package com.psp.qrcodeservice.dto;

public class HttpResponseDto {
    private String message;
    private Integer status;

    public HttpResponseDto(String message, Integer status) {
        this.message = message;
        this.status = status;
    }
}
