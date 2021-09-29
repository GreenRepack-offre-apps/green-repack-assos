package com.greenrepack.greenrepackassos.service.assos;

import java.io.Serializable;

import lombok.Data;

@Data
public class AssosInfo implements Serializable {
    private String rna;
    private String creationDateAssos;
    private String nom;
    private String description;
    private String emailAssos;
    private String telephone;
    private String siteweb;
}
