package com.guhecloud.cube.biz.work.center.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTask;
import com.guhecloud.cube.biz.work.center.server.param.WorkHomePageBatchParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkHomePageParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskQueryParam;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskBatchGroupTagVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskGroupTagVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskPageVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskTrendStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkTaskMapper extends BaseMapper<WorkTask> {

    Page<WorkTask> selectWorkPage(@Param("param") WorkTaskQueryParam param, @Param("userId") Long userId, Page<WorkTaskQueryParam> page);

    Page<WorkTask> selectDonePage(@Param("param") WorkTaskQueryParam param, @Param("userId") Long userId, Page<WorkTaskQueryParam> page);

    Long selectAllWorkNumTotal(@Param("period") Period period);

    Long selectWorkNumTotal(@Param("userId") Long userId);

    List<WorkBusinessTypeStatisticsVO> selectAllWorkBusinessTypeWorkStatistics(@Param("period") Period period);

    List<WorkBusinessTypeStatisticsVO> selectWorkBusinessTypeWorkStatistics(@Param("userId") Long userId);

    List<WorkBusinessTypeVO> selectWorkBusinessTypes();

    List<WorkBusinessTypeVO> selectWorkDoneBusinessTypes(@Param("userId") Long userId);

    List<WorkTaskTrendStatisticsVO> selectWorkTaskTrendStatistics(@Param("userId") Long userId, @Param("period") Period period);

    Integer selectAllowedHandoverTaskList(@Param("taskIdList") List<Long> taskIdList);

    List<WorkTaskGroupTagVO> selectWorkTaskNumTotal(@Param("status") String status, @Param("param") WorkHomePageParam param);

    List<WorkTaskBatchGroupTagVO> selectWorkTaskNumBatch(@Param("param") WorkHomePageBatchParam param);

    Page<WorkTaskPageVO> selectTask(@Param("page") Page<?> page, @Param("param") WorkTaskQueryParam param);

    List<Integer> selectHandoverRecordTenant();

    List<HandoverRecordVO> getOverdueTask(@Param("period") Period period, @Param("value") Integer value);
}
