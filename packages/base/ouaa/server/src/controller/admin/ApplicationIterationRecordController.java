package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationRecordParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationRecordQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationIterationRecordService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationIterationRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用迭代记录
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/application/iteration/record")
public class ApplicationIterationRecordController {
    private final ApplicationIterationRecordService applicationIterationRecordService;

    /**
     * 新增应用迭代记录
     */
    @PostMapping("/save/or/update")
    public boolean saveOrUpdate(@RequestBody ApplicationIterationRecordParam param) {
        return applicationIterationRecordService.saveOrUpdate(param);
    }

    /**
     * 删除应用迭代记录
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestParam("id") Long id) {
        return DatabaseContextHolder.softDelete(applicationIterationRecordService, id);
    }

    /**
     * 获取迭代记录
     */
    @GetMapping("/get")
    public Pagination<ApplicationIterationRecordVO> getList(ApplicationIterationRecordQueryParam param) {
        return applicationIterationRecordService.getList(param);
    }

    /**
     * 获取迭代记录详情
     */
    @GetMapping("/get/detail")
    public ApplicationIterationRecordVO getDetail(@RequestParam("id") Long id) {
        return applicationIterationRecordService.getDetail(id);
    }

    /**
     * 获取指定应用上次迭代版本
     */
    @GetMapping("/last/version")
    public String getLastVersion(@RequestParam("applicationId") Long applicationId) {
        return applicationIterationRecordService.getLastVersion(applicationId);
    }

}
