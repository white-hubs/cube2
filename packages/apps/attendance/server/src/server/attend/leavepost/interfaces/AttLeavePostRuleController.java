package com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.application.commandgateway.AttLeavePostRuleCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.application.querygateway.AttLeavePostRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.param.AttLeavePostRulePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.param.AttLeavePostRuleParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.result.AttLeavePostRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.result.AttLeavePostRulePageResult;
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
 * 离岗规则控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostRuleController {
    private final AttLeavePostRuleQueryService attLeavePostRuleQueryService;
    private final AttLeavePostRuleCommandService attLeavePostRuleCommandService;

    /**
     * 离岗规则分页列表
     *
     * @param param 条件对象
     *
     * @return 分页列表
     */
    @GetMapping("/att/leavePost/rule/page")
    public Pagination<AttLeavePostRulePageResult> leavePostRulePage(AttLeavePostRulePageParam param) {
        return attLeavePostRuleQueryService.leavePostRulePage(param);
    }

    /**
     * 离岗规则详情
     *
     * @param id 规则id
     *
     * @return 详情
     */
    @GetMapping("/att/leavePost/rule/detail")
    public AttLeavePostRuleDetailResult leavePostRuleDetail(Long id) {
        return attLeavePostRuleQueryService.leavePostRuleDetail(id);
    }

    /**
     * 新增离岗规则
     *
     * @param param 规则
     *
     * @return 规则id
     */
    @PostMapping("/att/leavePost/rule/add")
    public Long leavePostRuleAdd(@RequestBody @Valid AttLeavePostRuleParam param) {
        return attLeavePostRuleCommandService.leavePostRuleAdd(param);
    }

    /**
     * 编辑离岗规则
     *
     * @param param 规则
     *
     * @return 规则id
     */
    @PostMapping("/att/leavePost/rule/edit")
    public Long leavePostRuleEdit(@RequestBody @Valid AttLeavePostRuleParam param) {
        return attLeavePostRuleCommandService.leavePostRuleEdit(param);
    }

    /**
     * 删除离岗规则
     *
     * @param id 主键id
     *
     * @return 规则id
     */
    @DeleteMapping("/att/leavePost/rule/remove")
    public Long leavePostRuleRemove(Long id) {
        return attLeavePostRuleCommandService.leavePostRuleRemove(id);
    }
}
