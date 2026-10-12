package com.guhecloud.cube.srv.gen.product.market.server.pricing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：采价预警记录 Repository接口
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Repository
public interface GoodsPriceWarnRecordMapper extends BaseMapper<GoodsPriceWarnRecord> {

    /**
     * 预警处理状态统计
     *
     * @param param
     *
     * @return
     */
    List<CommonStatVO> goodsPriceWarnStatusStatByTime(@Param("param") GoodsPriceWarnStatParam param);
}