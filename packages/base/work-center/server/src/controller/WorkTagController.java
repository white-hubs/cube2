package com.guhecloud.cube.biz.work.center.server.controller;

import com.guhecloud.cube.biz.work.center.server.entity.WorkTag;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagModifyParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagQueryParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagReferBusinessTypeModifyParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagUpdateParam;
import com.guhecloud.cube.biz.work.center.server.service.WorkTagService;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTagVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
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
 * 工作标签接口管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTagController {

    private final WorkTagService workTagService;

    /**
     * 工作任务标签保存
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/work-tags")
    public Long save(@Valid @RequestBody WorkTagModifyParam param) {
        return this.workTagService.save(param);
    }

    /**
     * 工作任务标签修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/work-tags/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody WorkTagUpdateParam param) {
        this.workTagService.update(id, param);
    }

    /**
     * 工作任务标签列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/work-tags")
    public List<WorkTagVO> listWorkTags(@Valid WorkTagQueryParam param) {
        List<WorkTag> workTags = this.workTagService.listWorkTags(param);
        return ObjectUtils.copying(workTags, WorkTagVO.class);
    }

    /**
     * 工作任务标签删除
     *
     * @param id
     */
    @DeleteMapping("/work-tags/{id}")
    public void removeById(@PathVariable("id") Long id) {
        this.workTagService.removeById(id);
    }

    /**
     * 工作任务标签关联业务类型
     *
     * @param workTagCode
     * @param param
     *
     * @return
     */
    @PostMapping("/work-tags/{workTagCode}/refer/business-type")
    public Long saveWorkTagReferBusinessType(@PathVariable("workTagCode") String workTagCode,
                                             @Valid @RequestBody WorkTagReferBusinessTypeModifyParam param
                                            ) {
        return this.workTagService.saveReferBusinessType(workTagCode, param);
    }
}
