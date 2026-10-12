package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectStandardQueryParam extends Paging {

    //标准名称
    private String keyword;

    //对象分类
    private String targetCategory;

    //对象类型
    private String targetType;
    // 类型使用eq比对
    private Boolean targetTypeEq;

    //标准详情
    private Boolean flagDetails;
    // 主键id集合
    private List<Long> idList;

}
