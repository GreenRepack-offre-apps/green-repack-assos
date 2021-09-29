package com.greenrepack.greenrepackassos.service.accessibility;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessAssosRequest implements Serializable {
    private String rna;
    private String password;
}
