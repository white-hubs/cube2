package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeData;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalIotRealTimeDataParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalIotRealTimeDataVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.RealTimeDataAbnormalPageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.StatusStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-21  17:37
 * @Description:
 * @Version: 1.0
 */
public interface MedicalIotRealTimeDataService {
    boolean saveOrUpdate(MedicalIotRealTimeData param);
    /**
     * 实时监测分页
     */
    Pagination<MedicalIotRealTimeDataVO> getRealTimeDataPage(MedicalIotRealTimeDataParam param);
    /**
     * 状态统计
     */
    StatusStatisticsVO statusStatistics();
    /**
     * 实时监测异常指标分页
     */
    Pagination<RealTimeDataAbnormalPageVO> getRealTimeDataAbnormalPage(Paging param);
    /**查询实时监测详情*/
    MedicalIotRealTimeDataVO getRealTimeDataDetails(Long id);
}
