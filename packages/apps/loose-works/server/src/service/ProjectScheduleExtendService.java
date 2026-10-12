package com.guhecloud.cube.srv.gen.loose.works.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.param.ProjectScheduleParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectScheduleExtendPO;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/21 15:28
 * @Description: 工程监管扩展信息服务层
 */
public interface ProjectScheduleExtendService extends IService<ProjectScheduleExtendPO> {

    /**
     * 新增或编辑工程监管扩展信息
     *
     * @param id
     * @param param
     */
    void saveOrUpdateProjectScheduleExtend(Long id, ProjectScheduleParam param);

    /**
     * 扩展信息
     *
     * @param vo
     */
    void extendInfo(ProjectScheduleDetailVO vo);

}
