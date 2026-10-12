package com.guhecloud.cube.biz.work.center.server.controller;

import com.guhecloud.cube.biz.work.center.server.param.WorkBusinessUrlModifyParam;
import com.guhecloud.cube.biz.work.center.server.service.WorkBusinessUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作业务路径配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkBusinessUrlController {

    private final WorkBusinessUrlService workBusinessUrlService;

    /**
     * 新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/work-business-url")
    public Long save(@Valid @RequestBody WorkBusinessUrlModifyParam param) {
        return this.workBusinessUrlService.save(param);
    }

    /**
     * 修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/work-business-url/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody WorkBusinessUrlModifyParam param) {
        this.workBusinessUrlService.update(id, param);
    }
}
