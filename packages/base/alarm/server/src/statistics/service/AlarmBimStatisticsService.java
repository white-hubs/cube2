package com.guhecloud.cube.base.alarm.server.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmRecordAreaCountParam;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimApplicationStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimDeviceStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimSituationVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsDeviceTypeVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsLevelVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsRecordStatusVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsSituationVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsStatusVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsTopChainVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsTrendVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmRecordAreaCountVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmUserHandlerTimeoutRateStatisticsVO;

import java.util.List;

public interface AlarmBimStatisticsService {

    /**
     * 报警记录分页列表
     *
     * @param param
     *
     * @return
     */
    Page<AlarmRecordPO> getAlarmRecordPage(AlarmBimStatisticsQueryParam param);

    /**
     * 报警统计
     * 报警处理占比,报警真实性统计
     *
     * @param param
     *
     * @return
     */
    AlarmBimStatisticsVO getAlarmBimStatistics(AlarmBimStatisticsQueryParam param);

    /**
     * 报警级别统计
     * 报警级别统计报警数量(含环比)
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsLevelVO> getAlarmBimStatisticsLevel(AlarmBimStatisticsQueryParam param);

    List<AlarmBimStatisticsDeviceTypeVO> getAlarmBimStatisticsDeviceType(AlarmBimStatisticsQueryParam param);

    /**
     * 报警趋势
     * 报警趋势折线图统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsTrendVO> getAlarmBimStatisticsTrend(AlarmBimStatisticsQueryParam param);

    /**
     * 报警事件top5统计
     * 报警事件top5统计(含与上期环比)
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsEventTopFive(AlarmBimStatisticsQueryParam param);

    /**
     * 报警类型态势统计
     *
     * @param param
     *
     * @return
     */
    AlarmBimStatisticsSituationVO getAlarmTypeSituation(AlarmBimStatisticsQueryParam param);

    /**
     * 报警处理排名top5统计
     * 按人员处理排名
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsUserHandlerTopFive(AlarmBimStatisticsQueryParam param);

    /**
     * 报警区域top5统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsAreaTopFive(AlarmBimStatisticsQueryParam param);

    /**
     * 报警设备top5统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsDeviceTopFive(AlarmBimStatisticsQueryParam param);

    /**
     * 报警记录状态统计(报警数,未处理数,超时报警数)
     *
     * @param param
     *
     * @return
     */
    AlarmBimStatisticsRecordStatusVO getAlarmBimStatisticsRecordStatus(AlarmBimStatisticsQueryParam param);

    /**
     * 报警状态统计
     * 含报警总数,环比，报警状态分组统计
     *
     * @param param
     *
     * @return
     */
    AlarmBimStatisticsStatusVO getAlarmBimStatisticsStatus(AlarmBimStatisticsQueryParam param);

    /**
     * 获取报警对象数量
     *
     * @param param
     *
     * @return
     */
    Integer getAlarmTargetCount(AlarmBimStatisticsQueryParam param);

    /**
     * 报警对象趋势统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsTrendVO> getAlarmTargetTrend(AlarmBimStatisticsQueryParam param);

    /**
     * 报警对象报警数量统计
     *
     * @param param
     *
     * @return
     */
    AlarmBimStatisticsStatusVO getAlarmTargetStatistics(AlarmBimStatisticsQueryParam param);

    /**
     * 区域报警统计
     *
     * @param param 区域id集合
     *
     * @return 区域报警统计
     */
    List<AlarmRecordAreaCountVO> getAlarmAreaCount(AlarmRecordAreaCountParam param);

    /**
     * 设备报警分组统计
     *
     * @return
     */
    List<AlarmBimDeviceStatisticsVO> getDeviceGroupStatistics(AlarmBimStatisticsQueryParam param);

    /**
     * @Description 设备超温报警时间分组
     * @Author lrj
     */
    List<AlarmBimDeviceStatisticsVO> getTimeGroupStatistics(AlarmBimStatisticsQueryParam param);

    //应用分组统计
    List<AlarmBimApplicationStatisticsVO> getApplicationGroupStatistics(AlarmBimStatisticsQueryParam param);

    /**
     * 报警情况统计
     * 报警总数,处理率,响应率,正确率,超时率
     *
     * @param param
     *
     * @return
     */
    AlarmBimSituationVO getAlarmSituationRatio(AlarmBimStatisticsQueryParam param);

    /**
     * 报警总处理超时率
     *
     * @param param
     *
     * @return
     */
    AlarmHandlerTimeoutRateStatisticsVO getAlarmHandlerTimeoutRate(AlarmBimStatisticsQueryParam param);

    /**
     * 按用户统计报警处理超时率
     *
     * @param param
     *
     * @return
     */
    List<AlarmUserHandlerTimeoutRateStatisticsVO> getAlarmUserHandlerTimeoutRate(AlarmBimStatisticsQueryParam param);
}
