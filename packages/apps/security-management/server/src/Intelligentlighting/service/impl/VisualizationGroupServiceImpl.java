package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.dao.TimeControlLogMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.dao.VisualizationGroupMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.StatisticalChartParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.VisualizationGroupService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.GroupBaseDetailsMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GetGroupControlVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.PieChartValueVO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisualizationGroupServiceImpl implements VisualizationGroupService {
    private final DeviceBimSceneStdClient deviceBimSceneClient;
    private final VisualizationGroupMapper visualizationGroupMapper;
    private final GroupBaseDetailsMapper groupBaseDetailsMapper;
    private final TimeControlLogMapper controlLogMapper;

    @Override
    public GetGroupControlVO getGroupControl(StatisticalChartParam param) {
        Set<Long> ids = this.deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        if (ObjectUtils.isEmpty(ids)) {
            ids.add(-1L);
        }
        List<Long> list = new ArrayList<>(ids);
        GetGroupControlVO getGroupControlVO = new GetGroupControlVO();
        long deviceTotal = groupBaseDetailsMapper.selectCount(new LambdaQueryWrapper<GroupBaseDetails>()
                                                                      .in(GroupBaseDetails::getDeviceId, list));
        List<GroupBaseDetails> groupTotal = groupBaseDetailsMapper.selectList(new LambdaQueryWrapper<GroupBaseDetails>()
                                                                                      .in(GroupBaseDetails::getDeviceId, list)
                                                                                      .groupBy(GroupBaseDetails::getGroupControlId));
        getGroupControlVO.setDeviceTotal(ObjectUtils.isEmpty(deviceTotal) ? 0 : deviceTotal);
        getGroupControlVO.setGroupTotal(ObjectUtils.isEmpty(groupTotal) ? 0 : groupTotal.size());

        GetGroupControlVO statControl = visualizationGroupMapper.getGroupControl2(list);
        getGroupControlVO.setOpenTotal(statControl.getOpenTotal());
        getGroupControlVO.setCloseTotal(statControl.getCloseTotal());
        return getGroupControlVO;
    }

    @Override
    public List<PieChartValueVO> getActualTimeControl(StatisticalChartParam param) {
        List<PieChartValueVO> data = new ArrayList<>();
        BigDecimal value = new BigDecimal(0);
        long success = controlLogMapper.selectCount(new LambdaQueryWrapper<TimeControlLog>()
                                                            .between(TimeControlLog::getCreateTime, DateUtils.beginOfDay(LocalDateTime.now()), DateUtils.endOfDay(LocalDateTime.now()))
                                                            .eq(TimeControlLog::getResult, "成功"));
        PieChartValueVO chartValue = new PieChartValueVO("计划成功数", new BigDecimal(success), value);
        data.add(chartValue);
        long defeated = controlLogMapper.selectCount(new LambdaQueryWrapper<TimeControlLog>()
                                                             .between(TimeControlLog::getCreateTime, DateUtils.beginOfDay(LocalDateTime.now()), DateUtils.endOfDay(LocalDateTime.now()))
                                                             .eq(TimeControlLog::getResult, "失败"));
        chartValue = new PieChartValueVO("计划失败数", new BigDecimal(defeated), value);
        data.add(chartValue);
        Integer week = DateUtils.dayOfWeekLegacy(LocalDateTime.now());
        Integer total = visualizationGroupMapper.getActualTimeControl(LocalDateTime.now(), week);
        chartValue = new PieChartValueVO("待执行计划数", new BigDecimal(total - success - defeated), value);
        data.add(chartValue);
        Integer planTotal = visualizationGroupMapper.getActualTimePlanControl(LocalDateTime.now(), week);
        chartValue = new PieChartValueVO("时控计划", new BigDecimal(planTotal), value);
        data.add(chartValue);
        return data;
    }
}
