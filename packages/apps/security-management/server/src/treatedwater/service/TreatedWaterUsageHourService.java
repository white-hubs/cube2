package com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.entity.TreatedWaterUsageHour;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageHourParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageHourQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageStatQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterStatVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterTypeTrendStatVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterUsageHourVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：净化水使用量-小时表服务层
 *
 * @author Appet
 * @date 2023-10-30 15:08:36
 */
public interface TreatedWaterUsageHourService extends IService<TreatedWaterUsageHour> {

    String add(TreatedWaterUsageHourParam param);

    void update(String id, TreatedWaterUsageHourParam param);

    void delete(String id);

    void batchDelete(List<String> ids);

    TreatedWaterUsageHourVO getById(String id);

    Pagination<TreatedWaterUsageHourVO> getPage(TreatedWaterUsageHourQueryParam param);

    /**
     * 按类型统计用量
     *
     * @param param
     *
     * @return
     */
    List<TreatedWaterStatVO> getUsageStatByType(TreatedWaterUsageStatQueryParam param);

    /**
     * 按类型、时间统计用量
     *
     * @param param
     *
     * @return
     */
    List<TreatedWaterTypeTrendStatVO> getUsageStatByTypeAndTime(TreatedWaterUsageStatQueryParam param);

    /**
     * 按区域统计用量
     *
     * @param param
     *
     * @return
     */
    List<TreatedWaterStatVO> getUsageStatByArea(TreatedWaterUsageStatQueryParam param);

}