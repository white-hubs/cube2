package com.guhecloud.cube.base.alarm.server.flow.interfaces;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.AlarmLevelPlayConfigCommandService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.level.IncreaseAlarmLevelPlayConfigCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmLevelPlayConfigQueryService;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelPlayConfigVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 报警级别播放配置
 *
 * @author lanchun
 * @date 2024/12/4 11:47
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmLevelPlayConfigController {

    private final AlarmLevelPlayConfigCommandService alarmLevelPlayConfigCommandService;

    private final AlarmLevelPlayConfigQueryService alarmLevelPlayConfigQueryService;

    /**
     * 报警级别播放配置保存
     *
     * @param command 新增参数
     */
    @PostMapping("/alarm-level/play-config")
    public void increaseAlarmLevelPlayConfig(@Valid @RequestBody IncreaseAlarmLevelPlayConfigCommand command) {
        this.alarmLevelPlayConfigCommandService.increaseAlarmLevelPlayConfig(command);
    }

    /**
     * 报警级别播放配置集合
     *
     * @return AlarmLevelPlayConfigVO
     */
    @GetMapping("/alarm-level/play-configs")
    public List<AlarmLevelPlayConfigVO> getAlarmLevelPlayConfigs() {
        return this.alarmLevelPlayConfigQueryService.getAlarmLevelPlayConfigs();
    }

    /**
     * 获取报警级别的播放配置
     *
     * @param alarmLevelCode 报警级别编号
     *
     * @return AlarmLevelPlayConfigVO
     */
    @GetMapping("/alarm-level/play-config/{alarmLevelCode}")
    public AlarmLevelPlayConfigVO getAlarmLevelPlayConfig(@PathVariable("alarmLevelCode") String alarmLevelCode) {
        return this.alarmLevelPlayConfigQueryService.getAlarmLevelPlayConfig(alarmLevelCode);
    }
}
