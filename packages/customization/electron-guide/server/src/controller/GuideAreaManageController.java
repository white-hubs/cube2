package com.guhecloud.cube.srv.electron.guide.controller;

import com.guhecloud.cube.srv.electron.guide.param.GuideAreaPageParam;
import com.guhecloud.cube.srv.electron.guide.param.GuideAreaParam;
import com.guhecloud.cube.srv.electron.guide.param.GuideAreaQueryParam;
import com.guhecloud.cube.srv.electron.guide.service.GuideAreaManageService;
import com.guhecloud.cube.srv.electron.guide.vo.GuideAreaDetailsVO;
import com.guhecloud.cube.srv.electron.guide.vo.GuideAreaPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.TreeUtils;
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

import java.util.Collections;
import java.util.List;

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
public class GuideAreaManageController {
    private final GuideAreaManageService guideAreaManageService;

    @GetMapping("/public/guide/area/{id}")
    public GuideAreaDetailsVO getGuideAreaById(@PathVariable Long id) {
        return guideAreaManageService.getGuideAreaById(id);
    }

    @GetMapping("/public/guide/area/page")
    public Pagination<GuideAreaPageVO> getGuideAreaByPage(GuideAreaPageParam param) {
        return guideAreaManageService.getGuideAreaByPage(param);
    }

    @GetMapping("/public/guide/area/tree")
    public List<GuideAreaPageVO> getGuideAreaTree(@Valid GuideAreaQueryParam param) {
        List<GuideAreaPageVO> guideAreas = this.guideAreaManageService.getGuideAreas(param);
        if (CollectionUtils.isEmpty(guideAreas)) {
            return Collections.emptyList();
        }
        return TreeUtils.assembleTrees(guideAreas);
    }

    @PostMapping("/guide/area/add")
    public Boolean addPointManageById(@RequestBody GuideAreaParam param) {
        return guideAreaManageService.addGuideArea(param);
    }

    @PutMapping("/guide/area/update")
    public Boolean updatePointManage(@RequestBody GuideAreaParam param) {
        return guideAreaManageService.updateGuideArea(param);
    }

    @DeleteMapping("/guide/area/{id}")
    public Boolean deletePointManage(@PathVariable Long id) {
        return guideAreaManageService.deleteGuideArea(id);
    }
}
