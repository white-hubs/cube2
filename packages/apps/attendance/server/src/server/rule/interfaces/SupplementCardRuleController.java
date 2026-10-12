package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.SupplementCardRuleCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitSupplementCardRuleCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.SupplementCardRuleVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.SupplementCardRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.SupplementCardRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.query.SupplementCardRuleQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplementCardRuleController {

    private final SupplementCardRuleCommandService commandService;
    private final SupplementCardRuleQueryService queryService;

    @GetMapping("/card-rule/{id}")
    public SupplementCardRuleVO getById(@PathVariable("id") Long id) {
        SupplementCardRulePO cardRulePO = queryService.getById(id);
        SupplementCardRuleVO supplementCardRuleVO = ObjectUtils.copying(cardRulePO, SupplementCardRuleVO.class);
        supplementCardRuleVO.setTypes(Arrays.asList(cardRulePO.getType().split(",")));
        return supplementCardRuleVO;
    }

    @GetMapping("/card-rule/list")
    public List<SupplementCardRuleVO> list(SupplementCardRuleQueryParam param) {
        return queryService.getList(param);
    }

    @GetMapping("/card-rule/page")
    public Pagination<SupplementCardRuleVO> getPage(SupplementCardRuleQueryParam param) {
        return queryService.getPage(param);
    }

    @PostMapping("/card-rule")
    public Long saveOrUpdate(@Valid @RequestBody SubmitSupplementCardRuleCommand command) {
        return commandService.saveOrUpdate(command);
    }

    @DeleteMapping("/card-rule/{id}")
    public void delete(@PathVariable("id") Long id) {
        commandService.delete(id);
    }

}
