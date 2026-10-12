package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectItemTemplateQueryParam extends Paging {

    //标准名称
    private String keyword;

    //对象分类
    private String referType;

}
