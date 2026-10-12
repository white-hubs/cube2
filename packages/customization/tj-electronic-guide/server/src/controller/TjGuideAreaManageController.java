package com.guhecloud.cube.srv.tj.electronic.guide.controller;

import com.guhecloud.cube.srv.tj.electronic.guide.param.GuideAreaPageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.param.GuideAreaParam;
import com.guhecloud.cube.srv.tj.electronic.guide.service.TjGuideAreaManageService;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.GuideAreaDetailsVO;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.GuideAreaPageVO;
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

/**
 * 区域管理
 *
 * @Author: lijie
 * @CreateTime: 2024-01-08  11:40
 * @Description: 区域管理
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TjGuideAreaManageController {
    private final TjGuideAreaManageService tjGuideAreaManageService;

    @GetMapping("/public/tj/guide/area/{id}")
    public GuideAreaDetailsVO getGuideAreaById(@PathVariable Long id) {
        return tjGuideAreaManageService.getGuideAreaById(id);
    }

    @GetMapping("/public/tj/guide/area/page")
    public Pagination<GuideAreaPageVO> getGuideAreaByPage(GuideAreaPageParam param) {
        return tjGuideAreaManageService.getGuideAreaByPage(param);
    }

    @PostMapping("/tj/guide/area/add")
    public Boolean addPointManageById(@RequestBody GuideAreaParam param) {
        return tjGuideAreaManageService.addGuideArea(param);
    }

    @PutMapping("/tj/guide/area/update")
    public Boolean updatePointManage(@RequestBody GuideAreaParam param) {
        return tjGuideAreaManageService.updateGuideArea(param);
    }

    @DeleteMapping("/tj/guide/area/{id}")
    public Boolean deletePointManage(@PathVariable Long id) {
        return tjGuideAreaManageService.deleteGuideArea(id);
    }
}
