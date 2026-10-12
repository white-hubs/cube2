package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyBimConfigPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyBimConfigVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyBimConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  09:58
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface PropertyBimConfigMapper extends BaseMapper<PropertyBimConfig> {
    Page<PropertyBimConfigVO> queryByPage(@Param("page") Page page, @Param("param") PropertyBimConfigPageParam param);
}
