package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.controller;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.SafetyCultivateParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.SafetyCultivateQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.SafetyCultivateService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.SafetyCultivateStatisticVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.SafetyCultivateVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wql
 * @describe 安全培训(二维码中转页)
 * @date 2024/3/19 19:33
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyCultivateController {
    private final SafetyCultivateService safetyCultivateService;

    /**
     * 新增安全培训记录
     *
     * @param param 安全培训记录
     *
     * @return 记录id
     */
    @PostMapping(value = "/safetyCultivate/save")
    public Long saveSafetyCultivate(@Valid @RequestBody SafetyCultivateParam param) {
        return safetyCultivateService.saveSafetyCultivate(param);
    }

    /**
     * 安全教育分页列表
     *
     * @return 安全教育培训记录
     */
    @GetMapping(value = "/safetyCultivate/page")
    public Pagination<SafetyCultivateVO> safetyCultivateList(SafetyCultivateQueryParam param) {
        return safetyCultivateService.safetyCultivateList(param);
    }

    /**
     * 安全教育统计
     *
     * @param param 统计参数
     *
     * @return 统计结果
     */
    @GetMapping(value = "/safetyCultivate/statistic")
    public SafetyCultivateStatisticVO safetyCultivateStatistics(SafetyCultivateQueryParam param) {
        return safetyCultivateService.safetyCultivateStatistics(param);
    }

    /**
     * 安全教育统计
     *
     * @param param 统计参数
     *
     * @return 统计结果
     */
    @GetMapping(value = "/public/safetyCultivate/statistic")
    public SafetyCultivateStatisticVO publicSafetyCultivateStatistics(SafetyCultivateQueryParam param) {
        return safetyCultivateStatistics(param);
    }

}
