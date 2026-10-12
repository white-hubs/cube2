package com.guhecloud.cube.biz.vmonitor.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecord;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordUserVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolPersonPointStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolTrendStatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 视频巡逻记录mapper接口
 *
 * @author lanchun
 * @date 2021/12/6 19:54
 */
@Repository
public interface CameraPatrolRecordMapper extends BaseMapper<CameraPatrolRecord> {

    List<String> selectPatrolUserNamesByLineId(@Param("cameraPatrolLineId") Long cameraPatrolLineId);

    Integer selectPatrolAbnormalNum(@Param("param") CameraPatrolLineStatisticsQueryParam param);

    Integer selectPatrolRecordNum(@Param("param") CameraPatrolLineStatisticsQueryParam param);

    List<CameraPatrolRecordUserVO> selectPatrolUserNameByLineIds(@Param("ids") List<Long> cameraPatrolLineIds);

    List<CameraPatrolTrendStatisticsVO> selectPatrolRecordHourTrend(@Param("param") CameraPatrolLineStatisticsQueryParam param);

    List<CameraPatrolPersonPointStatisticsVO> selectCameraPatrolPersonPointStatistics(
            @Param("param") CameraPatrolLineStatisticsQueryParam param
                                                                                     );
}