package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.KeyValueResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.commandgateway.AttHolidayRuleCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.querygateway.AttHolidayRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayRulePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayRuleParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRulePageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttLegalHolidayUtils;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 假期规则（类型）
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttHolidayRuleController {

    private final AttHolidayRuleQueryService attHolidayRuleQueryService;
    private final AttHolidayRuleCommandService attHolidayRuleCommandService;

    /**
     * 假期规则分页列表
     *
     * @param param 列表条件入参对象
     *
     * @return 分页列表
     */
    @GetMapping("/att/holiday/rule/page")
    public Pagination<AttHolidayRulePageResult> pageHolidayRule(AttHolidayRulePageParam param) {

        return attHolidayRuleQueryService.pageHolidayRule(param);
    }

    /**
     * 假期规则详情
     *
     * @param id id
     *
     * @return 详情
     */
    @GetMapping("/att/holiday/rule/detail")
    public AttHolidayRuleDetailResult detailHolidayRule(Long id) {

        return attHolidayRuleQueryService.detailHolidayRule(id);
    }

    /**
     * 假期规则新增
     *
     * @param param 假期规则
     */
    @PostMapping("/att/holiday/rule/add")
    public void addHolidayRule(@RequestBody AttHolidayRuleParam param) {

        attHolidayRuleCommandService.addHolidayRule(param);
    }

    /**
     * 假期规则编辑
     *
     * @param param 假期规则
     */
    @PutMapping("/att/holiday/rule/edit")
    public void editHolidayRule(@RequestBody AttHolidayRuleParam param) {

        attHolidayRuleCommandService.editHolidayRule(param);
    }

    /**
     * 假期规则删除
     *
     * @param id id
     */
    @DeleteMapping("/att/holiday/rule/remove")
    public void removeHolidayRule(Long id) {

        attHolidayRuleCommandService.removeHolidayRule(id);
    }

    /**
     * 查询假期规则下拉列表(假期类型
     *
     * @return 列表
     */
    @GetMapping("/att/holiday/rule/list")
    public List<AttHolidayRuleListResult> listHolidayRuleList() {
        return attHolidayRuleQueryService.listHolidayRuleList();
    }

    /**
     * 获取法定假期列表
     *
     * @return 列表
     */
    @GetMapping("/att/legal/holiday/list")
    public List<KeyValueResult> legalHolidayList() {
        return attHolidayRuleQueryService.legalHolidayList();
    }

    /**
     * 获取限额的假期
     *
     * @return 不限额的假期列表
     */
    @GetMapping("/att/holiday/rule/notLimit/list")
    public List<AttHolidayRuleListResult> listHolidayRuleListNotLimit() {
        AttHolidayRulePageParam param = new AttHolidayRulePageParam();
        param.setNotLimit(false);
        return attHolidayRuleQueryService.listHolidayRuleList(param);
    }

    /**
     * 清除法定假期缓存
     */
    @GetMapping("/att/holiday/legal/clear")
    public Boolean clearLegalHolidayCache() {
        AttLegalHolidayUtils.clearMapCache();
        return true;
    }
}
