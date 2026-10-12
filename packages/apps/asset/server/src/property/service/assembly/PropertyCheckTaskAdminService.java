package com.guhecloud.cube.srv.gen.property.management.server.property.service.assembly;

import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.property.management.client.property.param.assembly.PropertyCheckTaskAdminParam;
import com.guhecloud.cube.property.management.client.property.vo.assembly.PropertyCheckTaskAdminPageVO;

import java.util.List;

public interface PropertyCheckTaskAdminService {
    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    List<PropertyCheckTaskAdminPageVO> getPropertyCheckTaskAdminPage(OrganizationQueryParam param);

    /**
     * 新增
     *
     * @param param
     *
     * @return 新增结果
     */
    Boolean insert(PropertyCheckTaskAdminParam param);
}
