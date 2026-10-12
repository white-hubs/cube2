package com.guhecloud.cube.srv.gen.safety.management.controller;

import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmConfigVO;
import com.guhecloud.cube.srv.gen.safety.management.po.AlarmPushConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.AlarmPushConfigService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 报警配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmConfigController {

    private final AlarmPushConfigService alarmPushConfigService;

    /**
     * 根据报警类型获取已配置的推送级别
     *
     * @param alarmTypeCode
     *
     * @return
     */
    @GetMapping("/alarm-config/{alarmTypeCode}/levels")
    public List<AlarmConfigVO> getPushLevelCodes(@PathVariable("alarmTypeCode") String alarmTypeCode) {
        List<AlarmPushConfigPO> alarmPushConfigPOS = this.alarmPushConfigService.getAlarmPushConfigPOS(alarmTypeCode);
        if (CollectionUtils.isEmpty(alarmPushConfigPOS)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(alarmPushConfigPOS, AlarmConfigVO.class);
    }

    /**
     * 报警推送配置保存
     *
     * @param alarmTypeCode 报警类型编号
     * @param levelCode     报警级别编号
     * @param timeout       是否超时
     *
     * @return
     */
    @PutMapping("/alarm-config/{alarmTypeCode}/level/{levelCode}")
    public Long settingPushLevelCode(@PathVariable("alarmTypeCode") String alarmTypeCode,
                                     @PathVariable("levelCode") String levelCode,
                                     @RequestParam(required = false) Boolean timeout
                                    ) {
        return this.alarmPushConfigService.save(alarmTypeCode, levelCode, timeout);
    }

    /**
     * 移除报警推送配置
     *
     * @param alarmTypeCode 报警类型编号
     * @param levelCode     报警级别编号
     */
    @DeleteMapping("/alarm-config/{alarmTypeCode}/level/{levelCode}")
    public void removePushLevelCode(@PathVariable("alarmTypeCode") String alarmTypeCode, @PathVariable("levelCode") String levelCode) {
        this.alarmPushConfigService.remove(alarmTypeCode, levelCode);
    }
}
