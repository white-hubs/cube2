package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationIterationRecord;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationRecordParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationIterationRecordQueryParam;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationIterationRecordVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/5 17:53
 * @Description:
 */

public interface ApplicationIterationRecordService extends IService<ApplicationIterationRecord> {

    /**
     * 新增或更新应用迭代记录
     *
     * @param param
     *
     * @return
     */
    boolean saveOrUpdate(ApplicationIterationRecordParam param);

    /**
     * 获取应用迭代记录列表
     *
     * @param param
     *
     * @return
     */
    Pagination<ApplicationIterationRecordVO> getList(ApplicationIterationRecordQueryParam param);

    /**
     * 获取指定应用上次迭代版本
     *
     * @param applicationId
     *
     * @return
     */
    String getLastVersion(Long applicationId);

    /**
     * 获取应用迭代记录详情
     *
     * @param id
     *
     * @return
     */
    ApplicationIterationRecordVO getDetail(Long id);

    /**
     * 获取应用最新版本
     *
     * @param applicationIds
     *
     * @return
     */
    Map<Long, String> getApplicationVersion(List<Long> applicationIds);
}
