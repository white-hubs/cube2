package com.guhecloud.cube.srv.electron.guide.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.electron.guide.entity.PointType;
import com.guhecloud.cube.srv.electron.guide.param.PointTypeModifyParam;
import com.guhecloud.cube.srv.electron.guide.param.query.PointTypePageQueryParam;
import com.guhecloud.cube.srv.electron.guide.param.query.PointTypeQueryParam;
import com.guhecloud.cube.srv.electron.guide.service.PointTypeService;
import com.guhecloud.cube.srv.electron.guide.vo.PointTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
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

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PointTypeController {

    private final PointTypeService pointTypeService;

    /**
     * 新增点位类型
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/point-type")
    public Long save(@Valid @RequestBody PointTypeModifyParam param) {
        return this.pointTypeService.save(param);
    }

    /**
     * 修改点位类型
     *
     * @param id
     * @param param
     */
    @PutMapping("/point-type/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody PointTypeModifyParam param) {
        this.pointTypeService.update(id, param);
    }

    /**
     * 点位类型详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/point-type/{id}")
    public PointTypeVO getPointType(@PathVariable("id") Long id) {
        PointType pointType = this.pointTypeService.getById(id);
        return ObjectUtils.copying(pointType, PointTypeVO.class);
    }

    /**
     * 点位类型分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/point-type/page")
    public Pagination<PointTypeVO> getPointTypePage(@Valid PointTypePageQueryParam param) {
        Page<PointType> page = this.pointTypeService.getPage(param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page, PointTypeVO.class);
    }

    /**
     * 点位类型列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/point-types")
    public List<PointTypeVO> getPointTypes(@Valid PointTypeQueryParam param) {
        List<PointType> pointTypes = this.pointTypeService.getList(param);
        return ObjectUtils.copying(pointTypes, PointTypeVO.class);
    }

    /**
     * 点位类型列表(对外暴露的接口,无需鉴权)
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/public/point-types")
    public List<PointTypeVO> getPublicPointTypes(@Valid PointTypeQueryParam param) {
        return this.getPointTypes(param);
    }

    /**
     * 点位类型删除
     *
     * @param id
     */
    @DeleteMapping("/point-type/{id}")
    public void removePointType(@PathVariable("id") Long id) {
        this.pointTypeService.remove(id);
    }
}
