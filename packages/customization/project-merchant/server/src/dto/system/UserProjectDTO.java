package com.guhecloud.cube.deliver.project.merchant.dto.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserProjectDTO {

    private String id;
    private String code;
    private String name;
    private String abbr;
    private String longitude;
    private String latitude;
    private String provinceCode;
    private String cityCode;
    private String address;
    private String url;
    private String api;
    private String model;
    private String figures;
    private int acreage;
    private String acreageStr;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private LocalDateTime launchDate;
    private Boolean hasLaunch;

    private String deptId;
    private String deptName;
    private Boolean isDeptManager;
    private List<String> leadDeptIds;
    
//    private String postId;
//    private String postName;

    private List<PostDTO> postDTOS;

//    private List<MenuDTO> menus;
//    private Set<String> functionCodes;

}
