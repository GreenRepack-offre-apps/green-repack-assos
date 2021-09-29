package com.greenrepack.greenrepackassos.service;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponseData<T> implements Serializable {
    private String status;
    private T data;
}
