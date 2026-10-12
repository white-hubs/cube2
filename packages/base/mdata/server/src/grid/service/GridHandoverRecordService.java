package com.guhecloud.cube.biz.mdata.server.grid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridHandoverRecord;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverAddParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverRecordQueryParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverTakeOverParam;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridHandoverRecordDetailVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridHandoverRecordVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 网格交接记录 Service
 */
public interface GridHandoverRecordService extends IService<GridHandoverRecord> {
    /**
     * 发起交接
     */
    List<Long> addHandoverRecord(GridHandoverAddParam param);

    /**
     * 接班
     */
    void takeOver(GridHandoverTakeOverParam param);

    /**
     * 撤销交接
     */
    boolean cancel(Long id);

    /**
     * 获取当前用户交接班列表
     */
    Pagination<GridHandoverRecordVO> getHandoverRecord(GridHandoverRecordQueryParam param);

    /**
     * 获取交接班记录详情
     */
    GridHandoverRecordDetailVO getHandoverRecordDetail(Long id);
}
