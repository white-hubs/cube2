package com.guhecloud.cube.base.alarm.server.sat.controller;

import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmType;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmTypeTerminal;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmTypeModifyParam;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmTypeQueryParam;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmTypeService;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmTypeTerminalService;
import com.guhecloud.cube.base.alarm.server.sat.vo.SatAlarmTypeVO;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 报警类型接口
 *
 * @author lanchun
 * @date 2022/6/2 16:43
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatAlarmTypeController {

    private final SatAlarmTypeService alarmTypeService;
    private final SatAlarmTypeTerminalService alarmTypeTerminalService;
    private final FileClient fileClient;
    private final ApplicationClient applicationClient;

    /**
     * 报警类型新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-alarm-type")
    public Long save(@Valid @RequestBody SatAlarmTypeModifyParam param) {
        return this.alarmTypeService.save(param);
    }

    /**
     * 报警类型修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/sat-alarm-type/{id}")
    public void update(@PathVariable("id") @Min(value = 1, message = "报警类型主键不能小于1") Long id,
                       @Valid @RequestBody SatAlarmTypeModifyParam param
                      ) {
        this.alarmTypeService.update(id, param);
    }

    /**
     * 报警类型详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/sat-alarm-type/{id}")
    public SatAlarmTypeVO getAlarmType(@PathVariable("id") @Min(value = 1, message = "报警类型主键不能小于1") Long id) {
        SatAlarmType satAlarmType = this.alarmTypeService.getById(id);
        Assert.isNotFound(satAlarmType, "报警类型不存在");
        List<SatAlarmTypeTerminal> satAlarmTypeTerminals = this.alarmTypeTerminalService.listAlarmTypeTerminalsByAlarmTypeId(id);
        SatAlarmTypeVO alarmTypeVO = ObjectUtils.copying(satAlarmType, SatAlarmTypeVO.class);
        if (!CollectionUtils.isEmpty(satAlarmTypeTerminals)) {
            alarmTypeVO.setTerminals(satAlarmTypeTerminals.stream().map(SatAlarmTypeTerminal::getTerminal)
                                             .collect(Collectors.toList()));
        }
        List<FileVO> fileVOS = fileClient.getByBusinessKey(alarmTypeVO.getId());
        if (!CollectionUtils.isEmpty(fileVOS)) {
            alarmTypeVO.setFileVO(fileVOS.get(0));
        }
        return alarmTypeVO;
    }

    /**
     * 报警类型详情
     *
     * @param applicationCode
     *
     * @return
     */
    @GetMapping("/sat-alarm-type/by-application-code/{applicationCode}")
    public SatAlarmTypeVO getByApplicationCode(@PathVariable("applicationCode") String applicationCode) {
        SatAlarmType satAlarmType = this.alarmTypeService.getByApplicationCode(applicationCode);
        SatAlarmTypeVO alarmTypeVO = ObjectUtils.copying(satAlarmType, SatAlarmTypeVO.class);
        return alarmTypeVO;
    }

    /**
     * 报警类型列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/sat-alarm-types")
    public List<SatAlarmTypeVO> listAlarmTypes(@Valid SatAlarmTypeQueryParam param) {
        List<SatAlarmType> satAlarmTypes = this.alarmTypeService.listAlarmTypes(param);
        if (CollectionUtils.isEmpty(satAlarmTypes)) {
            return Collections.emptyList();
        }
        List<ApplicationDTO> tenantApplications = this.applicationClient
                .getTenantApplications(SessionContextHolder.getRequester().getTenant());
        if (CollectionUtils.isEmpty(tenantApplications)) {
            return Collections.emptyList();
        }
        Set<String> applicationCodes = tenantApplications.stream().map(ApplicationDTO::getCode).collect(Collectors.toSet());
        List<SatAlarmType> tenantSatAlarmType = satAlarmTypes.stream().filter(satAlarmType -> applicationCodes
                .contains(satAlarmType.getApplicationCode())).collect(Collectors.toList());
        return ObjectUtils.copying(tenantSatAlarmType, SatAlarmTypeVO.class);
    }

    /**
     * 报警类型删除
     *
     * @param id
     */
    @DeleteMapping("/sat-alarm-type/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "报警类型主键不能小于1") Long id) {
        this.alarmTypeService.remove(id);
    }
}
