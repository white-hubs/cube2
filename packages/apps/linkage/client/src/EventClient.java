package com.guhecloud.cube.biz.linkage.client;

import com.guhecloud.apps.linkage.metadata.param.EventActionTriggerParam;
import com.guhecloud.apps.linkage.metadata.vo.EventActionBeforeVO;
import com.guhecloud.apps.linkage.metadata.vo.EventActionVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "linkage")
public interface EventClient {

    /**
     * 获取事件执行动作
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/events/{id}/actions")
    List<EventActionVO> getEventActions(@PathVariable("id") Long id);

    /**
     * 获取事件动作前置配置
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/events/{id}/action/before/list")
    List<EventActionBeforeVO> getEventActionBeforeList(@PathVariable("id") Long id);

    /**
     * 手动触发事件动作
     *
     * @param id     事件id
     * @param params 触发动作
     */
    @PostMapping("/events/{id}/action/trigger")
    void triggerEventAction(@PathVariable("id") Long id, @Valid @RequestBody List<EventActionTriggerParam> params);
}
