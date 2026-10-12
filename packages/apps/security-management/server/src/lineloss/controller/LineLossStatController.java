package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossStatQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossStatService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossStatVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 线损统计控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossStatController {
    private final LineLossStatService lineLossStatService;

    /**
     * 分页查询线损统计
     *
     * @param param 查询参数
     *
     * @return 分页结果
     */
    @GetMapping("/lineLoss/stat/page")
    public Pagination<LineLossStatVO> pageLineLossStat(@Valid LineLossStatQueryParam param) {
        return lineLossStatService.pageLineLoss(param);
    }

    /**
     * 导出线损统计
     *
     * @param param    查询参数
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/lineLoss/stat/export")
    public void exportLineLossStat(@Valid LineLossStatQueryParam param,
                                   HttpServletRequest request,
                                   HttpServletResponse response
                                  ) {
        lineLossStatService.export(param, request, response);
    }
}
