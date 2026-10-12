package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 应急人员分类管理
 * @date 2024-01-22 10:08:36
 */
@Data
public class EmergencyUserCategoryQueryParam extends Paging {

    //名称
    private String name;
    //父级ID
    private Long parentId;
    //等级
    private Integer level;
    //序列
    private String sequence;

}