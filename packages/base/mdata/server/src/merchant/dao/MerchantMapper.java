package com.guhecloud.cube.biz.mdata.server.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.merchant.dto.MerchantDTO;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Merchant;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {

    Page<MerchantDTO> page(Page<?> page, @Param("param") MerchantQueryParam queryParam);

    /**
     * 根据关键字查找商户信息
     *
     * @param keyword 关键字
     *
     * @return 商户信息列表
     */
    List<Merchant> lookupMerchants(@Param("keyword") String keyword);

    /**
     * 根据关键字查找商户信息
     *
     * @param keyword 关键字
     *
     * @return 商户信息列表
     */
    Page<Merchant> lookupMerchantPage(Page<?> page, @Param("keyword") String keyword, @Param("merchantIds") List<Long> merchantIds);
}
