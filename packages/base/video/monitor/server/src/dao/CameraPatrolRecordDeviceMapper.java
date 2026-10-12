package com.guhecloud.cube.biz.vmonitor.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecordDevice;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolTopStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolTrendStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolPointStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolTrendStatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 视频巡逻设备记录mapper接口
 *
 * @author lanchun
 * @date 2021/12/6 19:53
 */
@Repository
public interface CameraPatrolRecordDeviceMapper extends BaseMapper<CameraPatrolRecordDevice> {

    List<CameraPatrolTrendStatisticsVO> selectCameraPatrolTrendStatistics(@Param("param") CameraPatrolTrendStatisticsQueryParam param);

    List<CameraPatrolPointStatisticsVO> selectCameraPatrolPointTop(@Param("param") CameraPatrolTopStatisticsQueryParam param);
}