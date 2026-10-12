package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.aggregate.AttClockRulesAggregate;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockRulesRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockDevicePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockLocationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockRulesParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttParamAnnotationValid;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 打卡规则服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockRuleCommandService {

    private final AttClockRulesRepository attClockRulesRepository;
    private final AttGroupMapper attGroupMapper;

    /**
     * 添加打卡规则
     *
     * @param param 打卡规则
     *
     * @return id
     */
    public Long addAttClockRules(AttClockRulesParam param) {
        // 校验
        param.validBusiness();
        // 拷贝
        AttClockRulesAggregate rulesAggregate = new AttClockRulesAggregate();
        BeanUtils.copyProperties(param, rulesAggregate);
        this.copyLocationAndDeviceList(param, rulesAggregate);

        Long id = IdWorker.getId();
        rulesAggregate.setId(id);
        // 插入
        attClockRulesRepository.insertClockRules(rulesAggregate);
        return id;
    }

    /**
     * 编辑打卡规则
     *
     * @param param 编辑入参
     *
     * @return id
     */
    public Long editAttClockRules(AttClockRulesParam param) {
        // 校验
        Assert.isParameterError(param.getId() == null, "id不能为空！");
        AttParamAnnotationValid.validThrowError(param);
        param.validBusiness();
        // 赋值
        AttClockRulesAggregate rulesAggregate = new AttClockRulesAggregate();
        BeanUtils.copyProperties(param, rulesAggregate);
        this.copyLocationAndDeviceList(param, rulesAggregate);

        attClockRulesRepository.updateClockRules(rulesAggregate);
        return param.getId();
    }

    /**
     * 删除打卡规则
     *
     * @param id 主键
     *
     * @return id
     */
    public Long removeAttClockRules(Long id) {
        List<AttGroupPO> groupPOList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                         .eq(AttGroupPO::getClockRuleId, id)
                                                                );
        if (groupPOList != null && !groupPOList.isEmpty()) {
            StringBuilder errorBuffer = new StringBuilder("考勤组：");
            for (AttGroupPO groupPO : groupPOList) {
                errorBuffer.append(groupPO.getName()).append(",");
            }
            errorBuffer.append("在使用该规则 无法删除！");
            throw new ServiceException(errorBuffer.toString());
        }
        attClockRulesRepository.deleteClockRules(id);
        return id;
    }

    private void copyLocationAndDeviceList(AttClockRulesParam param, AttClockRulesAggregate rulesAggregate) {
        if (param.getLocationList() != null && !param.getLocationList().isEmpty()) {
            List<AttClockLocationPO> locationPOList = new ArrayList<>();
            param.getLocationList().forEach(locationParam -> {
                AttClockLocationPO locationPO = AttClockLocationPO.builder().build();
                BeanUtils.copyProperties(locationParam, locationPO);
                locationPOList.add(locationPO);
            });
            rulesAggregate.setLocationList(locationPOList);
        }
        if (param.getDeviceList() != null && !param.getDeviceList().isEmpty()) {
            List<AttClockDevicePO> devicePOList = new ArrayList<>();
            param.getDeviceList().forEach(deviceParam -> {
                AttClockDevicePO devicePO = AttClockDevicePO.builder().build();
                BeanUtils.copyProperties(deviceParam, devicePO);
                devicePOList.add(devicePO);
            });
            rulesAggregate.setDeviceList(devicePOList);
        }
    }
}
