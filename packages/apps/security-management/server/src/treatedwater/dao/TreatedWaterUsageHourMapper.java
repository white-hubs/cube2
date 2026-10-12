package com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.entity.TreatedWaterUsageHour;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageStatQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：净化水使用量-小时表 Repository接口
 *
 * @author Appet
 * @date 2023-10-30 15:08:36
 */
@Repository
public interface TreatedWaterUsageHourMapper extends BaseMapper<TreatedWaterUsageHour> {

    /**
     * 按类型统计用量
     *
     * @param param
     *
     * @return
     */
    List<TreatedWaterStatVO> getUsageStatByType(@Param("param") TreatedWaterUsageStatQueryParam param);

    /**
     * 按类型、时间统计用量
     *
     * @param param
     *
     * @return
     */
    List<TreatedWaterStatVO> getUsageStatByTypeAndTime(@Param("param") TreatedWaterUsageStatQueryParam param);

    /**
     * 按区域统计用量
     *
     * @param param
     *
     * @return
     */
    List<TreatedWaterStatVO> getUsageStatByArea(@Param("param") TreatedWaterUsageStatQueryParam param);
}