package com.guhecloud.cube.srv.gen.safety.management.education.interfaces;

import com.guhecloud.cube.srv.gen.safety.management.education.application.EducationMarketTaskUrlService;
import com.guhecloud.cube.srv.gen.safety.management.education.application.commandgateway.command.ChangeEducationMarketUrlEnabledCommand;
import com.guhecloud.cube.srv.gen.safety.management.education.application.querygateway.query.EducationMarketTaskUrlQuery;
import com.guhecloud.cube.srv.gen.safety.management.education.interfaces.vo.EducationMarketTaskUrlVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author lanchun
 * @date 2024/4/11 9:32
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EducationMarketTaskUrlController {

    private final EducationMarketTaskUrlService educationMarketTaskUrlService;

    /**
     * 安全教育任务链接分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/education/market-task-urls")
    public Pagination<EducationMarketTaskUrlVO> getMarketTaskUrls(@Valid EducationMarketTaskUrlQuery query) {
        return this.educationMarketTaskUrlService.getMarketTaskUrls(query);
    }

    /**
     * 安全教育任务链接状态修改
     *
     * @param id
     * @param enabled
     */
    @PutMapping("/education/market-task-url/{id}/{enabled}")
    public void changeUrlEnabled(@PathVariable("id") Long id, @PathVariable("enabled") Boolean enabled) {
        ChangeEducationMarketUrlEnabledCommand changeEducationMarketUrlEnabledCommand = new ChangeEducationMarketUrlEnabledCommand();
        changeEducationMarketUrlEnabledCommand.setId(id);
        changeEducationMarketUrlEnabledCommand.setEnabled(enabled);
        this.educationMarketTaskUrlService.changeEnabled(changeEducationMarketUrlEnabledCommand);
    }

    /**
     * 安全教育任务链接详情
     *
     * @return
     */
    @GetMapping("/public/education/market-task-url/detail")
    public EducationMarketTaskUrlVO getMarketTaskUrlDetail(@RequestParam Long id) {
        return this.educationMarketTaskUrlService.getMarketTaskUrlDetail(id);
    }
}
