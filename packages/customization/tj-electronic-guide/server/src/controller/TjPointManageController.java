package com.guhecloud.cube.srv.tj.electronic.guide.controller;

import com.guhecloud.cube.srv.tj.electronic.guide.param.PointManagePageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.param.PointManageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.service.TjPointManageService;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.PointManageDetailsVO;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.PointManagePageVO;
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
 * 点位管理
 *
 * @Author: lijie
 * @CreateTime: 2024-01-08  11:40
 * @Description: 点位管理
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TjPointManageController {
    private final TjPointManageService tjPointManageService;

    @GetMapping("/public/tj/point/anage/{id}")
    public PointManageDetailsVO getPointManageById(@PathVariable Long id) {
        return tjPointManageService.getPointManageById(id);
    }

    @GetMapping("/public/tj/point/anage/page")
    public Pagination<PointManagePageVO> getPointManageByPage(PointManagePageParam param) {
        return tjPointManageService.getPointManageByPage(param);
    }

    @PostMapping("/tj/point/anage/add")
    public Boolean addPointManageById(@RequestBody PointManageParam param) {
        return tjPointManageService.addPointManage(param);
    }

    @PutMapping("/tj/point/anage/update")
    public Boolean updatePointManage(@RequestBody PointManageParam param) {
        return tjPointManageService.updatePointManage(param);
    }

    @DeleteMapping("/tj/point/anage/{id}")
    public Boolean deletePointManage(@PathVariable Long id) {
        return tjPointManageService.deletePointManage(id);
    }
}
