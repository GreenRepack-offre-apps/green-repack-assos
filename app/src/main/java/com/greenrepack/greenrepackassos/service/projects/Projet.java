package com.greenrepack.greenrepackassos.service.projects;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Projet {
    private String idproj;
    private String refAssos;
    private String titre;
    private String description;
    private Date datecreate;
    private Date datevalid;
    private String status;
    private String refusMsg;
    private double argentcollect;
}
