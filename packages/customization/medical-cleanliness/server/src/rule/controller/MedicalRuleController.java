package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleAllParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RulePageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.message.MedicalRuleDeletedMessage;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.message.MedicalRuleModifyMessage;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RulePageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
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

import java.util.List;

/**
 * 规则配置
 *
 * @author : lijie
 * @date : 2024-3-20
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalRuleController {
    private final MedicalRuleService medicalRuleService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     *
     * @author : lijie
     */
    @GetMapping("/rule/queryById/{id}")
    public RuleVO queryById(@PathVariable Long id) {
        return medicalRuleService.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param param:筛选条件
     *
     * @return 查询结果
     *
     * @author : lijie
     */
    @GetMapping("/rule/page")
    public Pagination<RulePageVO> queryByPage(RulePageParam param) {
        return medicalRuleService.queryByPage(param);
    }

    /**
     * 分页查询
     *
     * @param param:筛选条件
     *
     * @return 查询结果
     *
     * @author : lijie
     */
    @GetMapping("/rule/all")
    public List<RuleVO> queryByAll(RuleAllParam param) {
        return medicalRuleService.queryByAll(param);
    }

    /**
     * 新增数据
     *
     * @param param 新增数据
     *
     * @return 新增结果
     *
     * @author : lijie
     */
    @PostMapping("/rule/insert")
    public Long insert(@RequestBody RuleParam param) {
        Long id = medicalRuleService.insert(param);
        RocketMQContextHolder.send(MedicalRuleModifyMessage.TOPIC, new MedicalRuleModifyMessage(id, System.currentTimeMillis()));
        return id;
    }

    /**
     * 更新数据
     *
     * @param param 修改数据
     *
     * @return 修改结果
     *
     * @author : lijie
     */
    @PutMapping("/rule/update")
    public Boolean update(@RequestBody RuleParam param) {
        Boolean updateResult = medicalRuleService.update(param);
        if (updateResult) {
            RocketMQContextHolder.send(MedicalRuleModifyMessage.TOPIC, new MedicalRuleModifyMessage(param.getId(), System.currentTimeMillis()));
        }
        return updateResult;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 删除结果
     *
     * @author : lijie
     */
    @DeleteMapping("/rule/delete/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        Boolean deleteResult = medicalRuleService.deleteById(id);
        if (deleteResult) {
            RocketMQContextHolder.send(MedicalRuleDeletedMessage.TOPIC, new MedicalRuleDeletedMessage(id));
        }
        return deleteResult;
    }

    /**
     * 查询指定设备的属性
     *
     * @author: lijie
     * @date: 2024/9/19 18:18
     * @param:
     * @return:
     **/
    @GetMapping("/getNormAllByDevice")
    public void getNormAllByDevice() {
        medicalRuleService.getNormAllByDevice();
    }
}