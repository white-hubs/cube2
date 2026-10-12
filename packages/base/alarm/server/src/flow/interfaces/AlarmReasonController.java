package com.guhecloud.cube.base.alarm.server.flow.interfaces;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.AlarmReasonConfigCommandService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.reason.ChangeAlarmReasonConfigCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.reason.IncreaseAlarmReasonConfigCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmReasonConfigQueryService;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmReasonConfigQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmReasonConfigVO;
import com.guhecloud.platform.base.model.Pagination;
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

import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmReasonController {

    private final AlarmReasonConfigCommandService alarmReasonConfigCommandService;

    private final AlarmReasonConfigQueryService alarmReasonConfigQueryService;

    /**
     * 新增报警原因
     *
     * @param increaseAlarmReasonConfigCommand
     */
    @PostMapping("/alarm-reason")
    public void increaseAlarmReasonConfig(@Valid @RequestBody IncreaseAlarmReasonConfigCommand increaseAlarmReasonConfigCommand) {
        this.alarmReasonConfigCommandService.increaseAlarmReasonConfig(increaseAlarmReasonConfigCommand);
    }

    /**
     * 修改报警原因
     *
     * @param changeAlarmReasonConfigCommand
     */
    @PutMapping("/alarm-reason")
    public void changeAlarmReasonConfig(@Valid @RequestBody ChangeAlarmReasonConfigCommand changeAlarmReasonConfigCommand) {
        this.alarmReasonConfigCommandService.changeAlarmReasonConfig(changeAlarmReasonConfigCommand);
    }

    /**
     * 报警原因分页列表
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/alarm-reason/page")
    public Pagination<AlarmReasonConfigVO> getAlarmReasonConfigPage(@Valid AlarmReasonConfigQueryParam queryParam) {
        return this.alarmReasonConfigQueryService.queryAlarmReasonPage(queryParam);
    }

    /**
     * 报警原因列表
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/alarm-reasons")
    public List<AlarmReasonConfigVO> getAlarmReasonConfigs(@Valid AlarmReasonConfigQueryParam queryParam) {
        return this.alarmReasonConfigQueryService.queryAlarmReasons(queryParam);
    }

    /**
     * 删除报警原因
     *
     * @param id
     */
    @DeleteMapping("/alarm-reason/{id}")
    public void deleteAlarmReasonConfig(@PathVariable("id") Long id) {
        this.alarmReasonConfigCommandService.deleteAlarmReasonConfig(id);
    }
}
