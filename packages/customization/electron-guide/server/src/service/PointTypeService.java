package com.guhecloud.cube.srv.electron.guide.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.electron.guide.entity.PointType;
import com.guhecloud.cube.srv.electron.guide.param.PointTypeModifyParam;
import com.guhecloud.cube.srv.electron.guide.param.query.PointTypePageQueryParam;
import com.guhecloud.cube.srv.electron.guide.param.query.PointTypeQueryParam;

import java.util.List;

public interface PointTypeService extends IService<PointType> {

    Long save(PointTypeModifyParam param);

    void update(Long id, PointTypeModifyParam param);

    PointType getById(Long id);

    Page<PointType> getPage(PointTypePageQueryParam param);

    List<PointType> getList(PointTypeQueryParam param);

    void remove(Long id);
}
