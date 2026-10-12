package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.core.model.BizTagType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CenterBizTagTypeMapper extends BaseMapper<BizTagType> {

    int insertSelective(BizTagType record);

    int updateByPrimaryKeySelective(BizTagType record);

    int updateByPrimaryKey(BizTagType record);

    int batchInsert(@Param("list") List<BizTagType> list);

    @InterceptorIgnore(tenantLine = "true")
    List<BizTag> getTagByType(@Param("tagType") String tagType);
}