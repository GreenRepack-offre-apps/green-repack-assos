package com.greenrepack.greenrepackassos.service.assos;

import java.io.Serializable;

import lombok.Data;

@Data
public class AssosGet implements Serializable {
    private AssosResult result;
    AssosInfo info;

}
