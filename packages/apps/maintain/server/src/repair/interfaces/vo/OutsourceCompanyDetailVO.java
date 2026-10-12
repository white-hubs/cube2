package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/5/18 16:27
 * @description
 */
@Data
public class OutsourceCompanyDetailVO {
    private Long id;

    private String name;

    private List<Long> outsourceModelIdList;

    private List<String> outsourceScopeIdList;

    private String responsiblePerson;

    private String responsiblePersonPhone;

}
