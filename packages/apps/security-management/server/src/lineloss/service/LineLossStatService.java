package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossStatQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossStatVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LineLossStatService {
    /**
     * 分页查询线损统计
     *
     * @param param 查询参数
     *
     * @return 分页结果
     */
    Pagination<LineLossStatVO> pageLineLoss(LineLossStatQueryParam param);

    /**
     * 导出线损统计
     *
     * @param param    查询参数
     * @param request  请求
     * @param response 响应
     */
    void export(LineLossStatQueryParam param, HttpServletRequest request, HttpServletResponse response);
}
