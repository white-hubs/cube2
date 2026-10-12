package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/5/18 18:45
 * @description
 */
@Data
public class OutsourceCompanyVO {
    private Long id;
    private String name;
    private List<ModelViewVO> outsourceModel;
    private List<ModelViewVO> outsourceScope;
    private String responsiblePerson;
    private String responsiblePersonPhone;
}
