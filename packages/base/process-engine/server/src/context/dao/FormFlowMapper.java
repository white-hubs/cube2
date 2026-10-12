package com.guhecloud.cube.base.flow.context.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author bq
 * @date 2022/09/08
 */
@Mapper
public interface FormFlowMapper extends BaseMapper<FormFlowPO> {


    FormFlowDetailVO selectAutoRemoveDuplication(@Param("businessType") String businessType);
}
