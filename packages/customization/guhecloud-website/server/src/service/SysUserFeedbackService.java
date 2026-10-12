package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysUserFeedbackParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysUserFeedbackQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysUserFeedbackVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 用户留言Service
 */
public interface SysUserFeedbackService {

    /**
     * 分页查询
     */
    Pagination<SysUserFeedbackVO> page(SysUserFeedbackQueryParam param);

    /**
     * 详情
     */
    SysUserFeedbackVO detail(Long id);

    /**
     * 新增
     */
    void add(SysUserFeedbackParam param);
    /**
     * 删除
     */
    void delete(Long id);

}
