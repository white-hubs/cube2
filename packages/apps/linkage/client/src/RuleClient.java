package com.guhecloud.cube.biz.linkage.client;

import com.guhecloud.cube.biz.linkage.client.dto.RuleDTO;
import com.guhecloud.cube.biz.linkage.client.param.RuleCustomTriggerParam;
import com.guhecloud.cube.biz.linkage.client.param.RuleModifyParam;
import com.guhecloud.cube.biz.linkage.client.param.RuleTriggerParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "linkage")
public interface RuleClient {

    /**
     * 规则新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/rule")
    Long save(@Valid @RequestBody RuleModifyParam param);

    /**
     * 规则修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/rule/{id}")
    void update(@PathVariable("id") Long id, @Valid @RequestBody RuleModifyParam param);

    /**
     * 规则状态修改
     *
     * @param id
     * @param enabled
     */
    @PutMapping("/rule/enabled/{id}/{enabled}")
    void updateEnabled(@PathVariable("id") Long id, @PathVariable("enabled") Boolean enabled);

    /**
     * 规则详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/rule/{id}")
    RuleDTO getRule(@PathVariable("id") Long id);

    /**
     * 规则触发
     *
     * @param id    规则主键
     * @param param 触发参数
     */
    @PutMapping("/rule/trigger/{id}")
    void ruleTrigger(@PathVariable("id") Long id, @Valid @RequestBody RuleTriggerParam param);

    /**
     * 规则触发
     *
     * @param param
     */
    @PutMapping("/rule/trigger")
    void ruleTrigger(@Valid @RequestBody RuleCustomTriggerParam param);

    /**
     * 规则删除
     *
     * @param id
     */
    @DeleteMapping("/rule/{id}")
    void remove(@PathVariable("id") Long id);
}
