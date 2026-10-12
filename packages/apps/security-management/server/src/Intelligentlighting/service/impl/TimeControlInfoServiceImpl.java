package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.dao.TimeControlInfoMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlInfo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.enumeration.DeviceTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlDetailsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlInfoParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlInfoQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlDetailsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlInfoService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.DeviceDetailsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.TimeControlInfoVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseInfo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.GroupBaseDetailsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.GroupBaseInfoService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TimeControlInfoServiceImpl extends ServiceImpl<TimeControlInfoMapper, TimeControlInfo> implements TimeControlInfoService {

    private final GroupBaseInfoService groupBaseInfoService;
    private final TimeControlDetailsService timeControlDetailsService;
    private final GroupBaseDetailsService groupBaseDetailsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTimeControl(TimeControlInfoParam param) {

        long count = this.count(Wrappers.<TimeControlInfo>lambdaQuery()
                                        .eq(TimeControlInfo::getPlanName, param.getPlanName()));
        Assert.isConflict(count > 0, "计划名称重复");
        long id = IdWorker.getId();
        TimeControlInfo timeControlInfo = ObjectUtils.copying(param, TimeControlInfo.class);
        if (Objects.nonNull(timeControlInfo.getPlanEndTime())) {
            var dateTime = DateUtils.endOfDay(timeControlInfo.getPlanEndTime());
            // 实际dateTime=  2020-10-29 23:59:59
            var endTime = DateUtils.offset(DateUtils.endOfDay(dateTime), ChronoUnit.MILLIS, -999);
            timeControlInfo.setPlanEndTime(endTime);
        }
        timeControlInfo.setId(id);
        //回路数计算
        long deviceNum = 0;
        ArrayList<TimeControlDetails> timeControlDetailsList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(param.getDeviceDetails())) {
            List<TimeControlDetailsParam> deviceDetails = param.getDeviceDetails();
            //排序
            Integer order = 1;
            for (TimeControlDetailsParam deviceDetail : deviceDetails) {
                try {
                    if (deviceDetail.getObjectType().equals(DeviceTypeEnum.DEVICE_GROUP.name())) {
                        GroupBaseInfo byId = groupBaseInfoService.getById(deviceDetail.getDeviceObject());
                        deviceNum += byId.getNum();
                    } else {
                        deviceNum += 1;
                    }

                    TimeControlDetails timeControlDetails = ObjectUtils.copying(deviceDetail, TimeControlDetails.class);
                    timeControlDetails.setTimeControlId(id);
                    timeControlDetails.setSort(order);
                    timeControlDetailsList.add(timeControlDetails);
                    order += 1;
                } catch (Exception e) {
                    throw new ServiceException("规则对象:" + deviceDetail.getDeviceName() + "错误");
                }
            }
        }
        timeControlInfo.setNum(deviceNum);
        this.save(timeControlInfo);
        timeControlDetailsService.saveBatch(timeControlDetailsList);
    }

    @Override
    public void updateTimeControl(Long id, TimeControlInfoParam param) {
        TimeControlInfo one1 = this.getOne(Wrappers.<TimeControlInfo>lambdaQuery()
                                                   .eq(TimeControlInfo::getPlanName, param.getPlanName()));
        if (Objects.nonNull(one1)) {
            Assert.isConflict(!one1.getId().equals(id), "名称重复");
        }

        TimeControlInfo one = this.getById(id);
        ObjectUtils.copying(param, one);
        if (Objects.nonNull(one.getPlanEndTime())) {
            var dateTime = DateUtils.endOfDay(one.getPlanEndTime());
            // 实际dateTime=  2020-10-29 23:59:59
            var endTime = DateUtils.offset(DateUtils.endOfDay(dateTime), ChronoUnit.MILLIS, -999);
            one.setPlanEndTime(endTime);
        }
        //回路数计算
        long deviceNum = 0;
        ArrayList<TimeControlDetails> timeControlDetailsList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(param.getDeviceDetails())) {
            List<TimeControlDetailsParam> deviceDetails = param.getDeviceDetails();
            //排序
            Integer order = 1;
            for (TimeControlDetailsParam deviceDetail : deviceDetails) {
                if (deviceDetail.getObjectType().equals(DeviceTypeEnum.DEVICE_GROUP.name())) {
                    GroupBaseInfo byId = groupBaseInfoService.getById(deviceDetail.getDeviceObject());
                    deviceNum += byId.getNum();
                } else {
                    deviceNum += 1;
                }
                TimeControlDetails timeControlDetails = ObjectUtils.copying(deviceDetail, TimeControlDetails.class);
                timeControlDetails.setTimeControlId(one.getId());
                timeControlDetails.setSort(order);
                timeControlDetailsList.add(timeControlDetails);
                order += 1;
            }
        }
        one.setNum(deviceNum);

        this.updateById(one);
        timeControlDetailsService.remove(Wrappers.<TimeControlDetails>lambdaQuery()
                                                 .eq(TimeControlDetails::getTimeControlId, one.getId()));
        timeControlDetailsService.saveBatch(timeControlDetailsList);
    }

    @Override
    public void deleteTimeControl(Long id) {
        DatabaseContextHolder.softDelete(this, id);
        List<TimeControlDetails> list = timeControlDetailsService.list(Wrappers.<TimeControlDetails>lambdaQuery()
                                                                               .eq(TimeControlDetails::getTimeControlId, id));
        DatabaseContextHolder.softDelete(TimeControlDetails.class, list);
    }

    @Override
    public TimeControlInfoVo getTimeControlInfo(Long id) {
        TimeControlInfo byId = this.getById(id);
        TimeControlInfoVo timeControlInfoVo = ObjectUtils.copying(byId, TimeControlInfoVo.class);
        List<TimeControlDetails> list = timeControlDetailsService.list(Wrappers.<TimeControlDetails>lambdaQuery()
                                                                               .eq(TimeControlDetails::getTimeControlId, id)
                                                                               .orderByAsc(TimeControlDetails::getSort));
        List<DeviceDetailsVo> detailsVos = ObjectUtils.copying(list, DeviceDetailsVo.class);
        timeControlInfoVo.setDeviceDetails(detailsVos);
        return timeControlInfoVo;
    }

    @Override
    public Pagination<TimeControlInfoVo> getTimeControls(TimeControlInfoQueryParam param) {
        Page<TimeControlInfo> page = this.page(PagingUtils.convert(param), Wrappers.<TimeControlInfo>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), TimeControlInfo::getPlanName, param.getKeyword())
                .orderByDesc(BasicEntity::getUpdateTime));
        //数量统计
        page.getRecords().forEach(s -> {
            long total = 0;
            List<TimeControlDetails> list = timeControlDetailsService.list(Wrappers.<TimeControlDetails>lambdaQuery()
                                                                                   .eq(TimeControlDetails::getTimeControlId, s.getId()));
            for (TimeControlDetails timeControlDetails : list) {
                if (timeControlDetails.getObjectType().equals(DeviceTypeEnum.DEVICE_SINGLETON.name())) {
                    total += 1;
                } else {
                    long count = groupBaseDetailsService.count(Wrappers.<GroupBaseDetails>lambdaQuery()
                                                                       .eq(GroupBaseDetails::getGroupControlId, timeControlDetails.getDeviceObject()));
                    total += count;
                }
            }
            s.setNum(total);
        });

        return PagingUtils.convert(page, TimeControlInfoVo.class);
    }
}
