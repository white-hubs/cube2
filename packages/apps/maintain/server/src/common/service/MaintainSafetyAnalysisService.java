package com.guhecloud.cube.srv.gen.maintain.server.common.service;

import com.guhecloud.cube.srv.gen.maintain.server.common.vo.MaintainSafetyAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.WorkSituationVO;
import com.guhecloud.platform.base.model.Period;

public interface MaintainSafetyAnalysisService {
    /**
     * 安全分析-获取隐患-巡更-巡检数据
     *
     * @param period
     * @param type
     *
     * @return
     */
    MaintainSafetyAnalysisVO maintainSafetyAnalysis(Period period, String type);

    /**
     * 隐患排查数据统计
     *
     * @param period
     * @param type
     *
     * @return
     */
    WorkSituationVO hiddenDangerForSafetyAnalysis(Period period, String type);

    /**
     * 电子巡检数据统计
     *
     * @param period
     * @param type
     *
     * @return
     */
    WorkSituationVO inspectAnalysisSafetyAnalysis(Period period, String type, String result);

    /**
     * 电子巡检数据统计(带权限)
     *
     * @param period
     * @param type
     *
     * @return
     */
    WorkSituationVO inspectAnalysisPermission(Period period, String type, String result);

    /**
     * 电子巡更数据统计
     *
     * @param period
     * @param type
     *
     * @return
     */
    WorkSituationVO patrolTaskSafetyAnalysis(Period period, String type);

}
