package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyHistoryPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyHistoryListVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PropertyHistoryMapper extends BaseMapper<PropertyHistory> {
    Page<PropertyHistoryListVO> getPropertyHistoryPage(@Param("page") Page page, @Param("param") PropertyHistoryPageParam param);
}
