package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossReadQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossReadService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossReadRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 线损采集记录控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossReadController {
    private final LineLossReadService lineLossReadService;

    /**
     * 分页查询线损采集记录
     *
     * @param param 查询参数
     *
     * @return 分页结果
     */
    @GetMapping("/lineLoss/read/page")
    public Pagination<LineLossReadRecordVO> pageLineLossRead(@Valid LineLossReadQueryParam param) {
        return lineLossReadService.pageLossRead(param);
    }
}
