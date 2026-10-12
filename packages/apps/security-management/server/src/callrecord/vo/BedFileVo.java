package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BedFileVo {
    private Long id;
    private String bed;
    private String currentVisitId;
    private LocalDateTime dateHospitalization;
    private LocalDateTime dischargeDate;
    private String currentPatient;
    private String bedManagementDoctor;
    private String bedNurse;
    private Integer patientGender;
    private String attendingPhysician;
    private String pathologicalInformation;
    private String medicationInformation;
    private int age;
}
