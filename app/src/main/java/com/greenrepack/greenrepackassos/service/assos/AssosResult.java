package com.greenrepack.greenrepackassos.service.assos;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class AssosResult implements Serializable {
    private String idassos;
    private String rnaId;
    private Date datecreation;
}
