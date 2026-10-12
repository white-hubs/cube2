package com.guhecloud.cube.biz.mdata.server.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantStall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MerchantStallMapper extends BaseMapper<MerchantStall> {

    int saveBatch(@Param("list") List<MerchantStall> list);

}
