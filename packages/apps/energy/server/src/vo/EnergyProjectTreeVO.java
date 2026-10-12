package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * @author Appet
 * @Description 分项
 * @date 2023-03-16 10:08:36
 */
@Data
public class EnergyProjectTreeVO extends Tree.Simple<Long, EnergyProjectTreeVO> {

    //分项名称
    private String projectName;

}