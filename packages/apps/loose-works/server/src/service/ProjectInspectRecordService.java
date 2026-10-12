package com.guhecloud.cube.srv.gen.loose.works.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.BaseQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.param.ProjectInspectRecordParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectInspectRecordVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectInspectRecordPO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/11 17:10
 * @Description:
 */
public interface ProjectInspectRecordService extends IService<ProjectInspectRecordPO> {

    /**
     * 提交工程巡查记录
     *
     * @param param
     *
     * @return
     */
    Long submitProjectInspectRecord(ProjectInspectRecordParam param);

    /**
     * 分页查询工程巡查记录
     *
     * @param baseQuery
     *
     * @return
     */
    Pagination<ProjectInspectRecordVO> getPageProjectInspectRecord(BaseQuery baseQuery);

    /**
     * 查询工程巡查记录详情
     *
     * @param id
     *
     * @return
     */
    ProjectInspectRecordVO getProjectInspectRecordDetail(Long id);

    /**
     * 是否存在巡检异常
     *
     * @param id
     *
     * @return
     */
    Boolean isExitAbnormal(Long id);
}