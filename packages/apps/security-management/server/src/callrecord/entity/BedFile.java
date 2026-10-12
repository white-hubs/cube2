package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BedFile extends BasicEnhancedTenantEntity<Long> {
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
