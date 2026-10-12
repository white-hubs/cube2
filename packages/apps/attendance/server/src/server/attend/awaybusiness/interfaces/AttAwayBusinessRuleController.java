package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.application.commandgateway.AttAwayBusinessRuleCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.application.querygateway.AttAwayBusinessRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.param.AttAwayBusinessRulePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.param.AttAwayBusinessRuleParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.result.AttAwayBusinessRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.result.AttAwayBusinessRulePageResult;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 出差规则
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAwayBusinessRuleController {
    private final AttAwayBusinessRuleQueryService attAwayBusinessRuleQueryService;
    private final AttAwayBusinessRuleCommandService attAwayBusinessRuleCommandService;

    /**
     * 出差规则分页列表
     *
     * @param param 条件对象
     *
     * @return 分页列表
     */
    @GetMapping("/att/awayBusiness/rule/page")
    public Pagination<AttAwayBusinessRulePageResult> awayBusinessRulePage(AttAwayBusinessRulePageParam param) {
        return attAwayBusinessRuleQueryService.awayBusinessRulePage(param);
    }

    /**
     * 出差规则详情
     *
     * @param id 规则id
     *
     * @return 详情
     */
    @GetMapping("/att/awayBusiness/rule/detail")
    public AttAwayBusinessRuleDetailResult awayBusinessRuleDetail(Long id) {
        return attAwayBusinessRuleQueryService.awayBusinessRuleDetail(id);
    }

    /**
     * 新增出差规则
     *
     * @param param 规则
     *
     * @return 规则id
     */
    @PostMapping("/att/awayBusiness/rule/add")
    public Long awayBusinessRuleAdd(@RequestBody @Valid AttAwayBusinessRuleParam param) {
        return attAwayBusinessRuleCommandService.awayBusinessRuleAdd(param);
    }

    /**
     * 编辑出差规则
     *
     * @param param 规则
     *
     * @return 规则id
     */
    @PostMapping("/att/awayBusiness/rule/edit")
    public Long awayBusinessRuleEdit(@RequestBody @Valid AttAwayBusinessRuleParam param) {
        return attAwayBusinessRuleCommandService.awayBusinessRuleEdit(param);
    }

    /**
     * 删除出差规则
     *
     * @param id 主键id
     *
     * @return 规则id
     */
    @DeleteMapping("/att/awayBusiness/rule/remove")
    public Long awayBusinessRuleRemove(Long id) {
        return attAwayBusinessRuleCommandService.awayBusinessRuleRemove(id);
    }

}
