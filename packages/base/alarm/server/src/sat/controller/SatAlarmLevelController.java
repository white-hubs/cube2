package com.guhecloud.cube.base.alarm.server.sat.controller;

import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmLevel;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmLevelModifyParam;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmLevelService;
import com.guhecloud.cube.base.alarm.server.sat.vo.SatAlarmLevelVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 报警级别管理接口
 *
 * @author lanchun
 * @date 2022/6/2 15:13
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatAlarmLevelController {

    private final SatAlarmLevelService alarmLevelService;

    /**
     * 报警级别新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-alarm-level")
    public Long save(@Valid @RequestBody SatAlarmLevelModifyParam param) {
        return this.alarmLevelService.save(param);
    }

    /**
     * 报警级别修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/sat-alarm-level/{id}")
    public void update(@PathVariable("id") @Min(value = 1, message = "报警级别主键不能小于1") Long id,
                       @Valid @RequestBody SatAlarmLevelModifyParam param
                      ) {
        this.alarmLevelService.update(id, param);
    }

    /**
     * 报警级别详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/sat-alarm-level/{id}")
    public SatAlarmLevelVO getAlarmLevel(@PathVariable("id") @Min(value = 1, message = "报警级别主键不能小于1") Long id) {
        SatAlarmLevel satAlarmLevel = this.alarmLevelService.getById(id);
        Assert.isNotFound(satAlarmLevel, "报警级别不存在");
        return ObjectUtils.copying(satAlarmLevel, SatAlarmLevelVO.class);
    }

    /**
     * 报警级别列表
     *
     * @return
     */
    @GetMapping("/sat-alarm-levels")
    public List<SatAlarmLevelVO> listAlarmLevels(@RequestParam(value = "keyword", required = false) String keyword) {
        List<SatAlarmLevel> satAlarmLevels = this.alarmLevelService.listAlarmLevels(keyword);
        if (CollectionUtils.isEmpty(satAlarmLevels)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(satAlarmLevels, SatAlarmLevelVO.class);
    }

    /**
     * 报警级别列表(根据主键集合获取)
     *
     * @param ids
     *
     * @return
     */
    @GetMapping("/sat-alarm-levels/ids")
    public List<SatAlarmLevelVO> getByIds(@RequestParam("ids") Collection<Long> ids) {
        List<SatAlarmLevel> satAlarmLevels = this.alarmLevelService.listAlarmLevels(ids);
        return ObjectUtils.copying(satAlarmLevels, SatAlarmLevelVO.class);
    }

    /**
     * 报警级别删除
     *
     * @param id
     */
    @DeleteMapping("/sat-alarm-level/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "报警级别主键不能小于1") Long id) {
        this.alarmLevelService.remove(id);
    }
}
