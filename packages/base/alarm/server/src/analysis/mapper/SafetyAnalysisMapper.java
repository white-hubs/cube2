package com.guhecloud.cube.base.alarm.server.analysis.mapper;

import com.guhecloud.cube.base.alarm.server.analysis.param.SafetyAnalysisAlarmParam;
import com.guhecloud.cube.base.alarm.server.analysis.vo.AlarmHandlerRankCardVO1;
import com.guhecloud.cube.base.alarm.server.analysis.vo.AlarmLevelRatioCardVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SafetyAnalysisMapper {

    /**
     * 获取报警数、未处理数、已处理数
     *
     * @param param
     *
     * @return
     */
    SafetyAnalysisAlarmVO selectAlarmRecordTotal(@Param("param") SafetyAnalysisAlarmParam param);

    /**
     * 获取报警数根据应用类型分组
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelRatioCardVO> selectAlarmNumGroupByApplicationCode(@Param("param") SafetyAnalysisAlarmParam param);

    /**
     * 获取报警数根据等级分组
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelRatioCardVO> selectAlarmNumGroupByLevelCode(@Param("param") SafetyAnalysisAlarmParam param);

    /**
     * 获取报警数根据事件类型分组
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelRatioCardVO> selectAlarmNumGroupByEventCode(@Param("param") SafetyAnalysisAlarmParam param);

    /**
     * 获取指定应用报警数、响应数、处理数、超时数
     *
     * @param param
     *
     * @return
     */
    AlarmHandlerRankCardVO1 selectAlarmRespondHandlerTimeOutNum(@Param("param") SafetyAnalysisAlarmParam param);

    /**
     * 获取用户负责报警数、响应数、处理数、超时数
     *
     * @param param
     *
     * @return
     */
    List<AlarmHandlerRankCardVO1> selectAlarmUserRatioRank(@Param("param") SafetyAnalysisAlarmParam param);

    /**
     * 根据报警事件分组获取真实报警数和已处理报警数
     *
     * @param param
     *
     * @return
     */
    List<SafetyAnalysisAlarmVO2> selectAlarmEventCorrectRatio(@Param("param") SafetyAnalysisAlarmParam param);

    /**
     * 获取报警记录个数
     *
     * @param param
     *
     * @return
     */
    Long selectAlarmRecordCountNum(@Param("param") SafetyAnalysisAlarmParam param);
}
