package com.greenrepack.greenrepackassos.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResult<T> {
    private T result;
    private boolean hasError;
    private String msgError;

    public ApiResult(){
        result = null;
        hasError = false;
        msgError = "";
    }

}
