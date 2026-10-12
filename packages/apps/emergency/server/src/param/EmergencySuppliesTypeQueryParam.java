package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 应急物资类型管理
 * @date 2024-01-016 10:08:36
 */
@Data
public class EmergencySuppliesTypeQueryParam extends Paging {

    //名称
    private String name;
    //编号
    private String code;
    //状态
    private Boolean status;

}