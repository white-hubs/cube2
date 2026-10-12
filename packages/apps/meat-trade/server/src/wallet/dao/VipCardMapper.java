package com.guhecloud.cube.biz.meattrade.server.wallet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierVipCardDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.VipCard;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.VipCardQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 会员卡数据操作接口
 *
 * @author Woody
 * @date 2021/8/22
 */
@Mapper
public interface VipCardMapper extends BaseMapper<VipCard> {
    /**
     * 获取用户会员卡列表
     *
     * @param supplierIds 商户主键集合
     *
     * @return 会员卡列表
     */
    List<VipCard> getUserCards(@Param("supplierIds") Collection supplierIds);

    /**
     * 查询会员卡列表
     *
     * @param page  分页信息
     * @param param 查询参数
     *
     * @return 会员卡列表分页
     */
    Page<SupplierVipCardDTO> listCards(Page<?> page, @Param("param") VipCardQueryParam param,
                                       @Param("supplierIds") Collection<Long> supplierIds
                                      );
}
