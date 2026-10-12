package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigDeleteParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyBimConfigVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  09:52
 * @Description:
 * @Version: 1.0
 */
public interface PropertyBimConfigService {
    /**
     * 分页查询
     *
     * @param param: 筛选条件
     *
     * @return 查询结果
     *
     * @author : lijie
     */
    Pagination<PropertyBimConfigVO> queryByPage(PropertyBimConfigPageParam param);

    /**
     * 新增数据
     *
     * @param param: 新增数据
     *
     * @return 新增结果
     *
     * @author : lijie
     */
    Boolean insert(PropertyBimConfigParam param);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 删除结果
     *
     * @author : lijie
     */
    Boolean deleteById(PropertyBimConfigDeleteParam ids);
}
