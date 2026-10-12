package com.guhecloud.cube.srv.electron.guide.controller;

import com.guhecloud.cube.srv.electron.guide.entity.PointType;
import com.guhecloud.cube.srv.electron.guide.param.PointManagePageParam;
import com.guhecloud.cube.srv.electron.guide.param.PointManageParam;
import com.guhecloud.cube.srv.electron.guide.service.PointManageService;
import com.guhecloud.cube.srv.electron.guide.service.PointTypeService;
import com.guhecloud.cube.srv.electron.guide.vo.PointManageDetailsVO;
import com.guhecloud.cube.srv.electron.guide.vo.PointManagePageVO;
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

import java.util.Objects;

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
public class PointManageController {
    private final PointManageService pointManageService;
    private final PointTypeService pointTypeService;

    @GetMapping("/public/point/anage/{id}")
    public PointManageDetailsVO getPointManageById(@PathVariable Long id) {
        PointManageDetailsVO pointManageDetailsVO = pointManageService.getPointManageById(id);
        if (Objects.isNull(pointManageDetailsVO)) {
            return null;
        }
        if (Objects.isNull(pointManageDetailsVO.getTypeId())) {
            return pointManageDetailsVO;
        }
        PointType pointType = this.pointTypeService.getById(pointManageDetailsVO.getTypeId());
        if (Objects.nonNull(pointType)) {
            pointManageDetailsVO.setTypeName(pointType.getName());
            pointManageDetailsVO.setTypeFilePath(pointType.getFilePath());
        }
        return pointManageDetailsVO;
    }

    @GetMapping("/public/point/anage/page")
    public Pagination<PointManagePageVO> getPointManageByPage(PointManagePageParam param) {
        return pointManageService.getPointManageByPage(param);
    }

    @PostMapping("/point/anage/add")
    public Boolean addPointManageById(@RequestBody PointManageParam param) {
        return pointManageService.addPointManage(param);
    }

    @PutMapping("/point/anage/update")
    public Boolean updatePointManage(@RequestBody PointManageParam param) {
        return pointManageService.updatePointManage(param);
    }

    @DeleteMapping("/point/anage/{id}")
    public Boolean deletePointManage(@PathVariable Long id) {
        return pointManageService.deletePointManage(id);
    }
}
