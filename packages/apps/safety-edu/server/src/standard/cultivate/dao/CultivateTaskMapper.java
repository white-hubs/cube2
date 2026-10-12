package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskPublicQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateCommonStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateMyTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateUserStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.query.SafetyEducationStatisticsQuery;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：培训任务 Repository接口
 *
 * @author Appet
 * @date 2023-04-18 15:08:36
 */
@Repository
public interface CultivateTaskMapper extends BaseMapper<CultivateTask> {

    CultivateTaskStatVO getCultivateTaskStat(@Param("period") Period period);

    CultivateTaskStatVO getCultivateTaskPermissionStat(@Param("period") Period period);

    List<CultivateCommonStatVO> getCultivateThemeStat(@Param("period") Period period);

    List<CultivateCommonStatVO> getCultivateTrendStat(@Param("period") Period period);

    CultivateUserStatVO getTrainUserStat(@Param("period") Period period);

    List<CultivateCommonStatVO> getCultivateTargetStat(@Param("period") Period period);

    Page<CultivateMyTaskVO> getMyTask(Page page, @Param("param") CultivateTaskQueryParam param);

    Page<CultivateMyTaskVO> getPublicMyTaskPage(Page page, @Param("param") CultivateTaskPublicQueryParam param);

    @InterceptorIgnore(tenantLine = "on")
    List<CultivateTaskPageVO> getCultivateStandardList(@Param("tenant") Integer tenant, @Param("param") CultivateTaskQueryParam param);

    /**
     * 培训任务主题统计
     *
     * @param query
     *
     * @return
     */
    List<SafetyEducationThemeRatioVO> selectCultivateThemeRatio(@Param("query") SafetyEducationStatisticsQuery query);

    /**
     * 培训任务数量
     *
     * @param query
     *
     * @return
     */
    Integer selectCultivateCount(@Param("query") SafetyEducationStatisticsQuery query);

    /**
     * 培训任务参与人数
     *
     * @param query
     *
     * @return
     */
    Long selectCultivateUserCount(@Param("query") SafetyEducationStatisticsQuery query);
}