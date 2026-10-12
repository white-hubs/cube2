package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleDataOrrectionPageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleDataOrrectionParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.RuleDataOrrectionService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleDataOrrectionVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 指标数据修正
 *
 * @Author: lijie
 * @CreateTime: 2024-06-21  14:53
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleDataOrrectionController {
    private final RuleDataOrrectionService ruleDataOrrectionService;

    /**
     * 查询所有修正记录
     */
    @GetMapping("/ruleDataOrrection/getAll")
    public List<RuleDataOrrectionVO> getAll() {
        return ruleDataOrrectionService.getAll();
    }

    /**
     * 分页查询修正记录
     */
    @GetMapping("/ruleDataOrrection/getPage")
    public Pagination<RuleDataOrrectionVO> getPage(RuleDataOrrectionPageParam param) {
        return ruleDataOrrectionService.getPage(param);
    }

    /**
     * 新增修正记录
     */
    @PostMapping("/ruleDataOrrection/insert")
    public Boolean insert(@RequestBody RuleDataOrrectionParam param) {
        return ruleDataOrrectionService.insert(param);
    }

    /**
     * 取消记录
     */
    @PutMapping("/ruleDataOrrection/updateStatus/{id}")
    public Boolean updateStatus(@PathVariable("id") Long id) {
        return ruleDataOrrectionService.updateStatus(id);
    }
}
