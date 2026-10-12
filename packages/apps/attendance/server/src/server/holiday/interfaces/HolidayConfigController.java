package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.HolidayCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.ChangeHolidayConfigCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.HolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayDetailDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayTypePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.vo.HolidayConfigPreviewVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Nullable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/11/21 16:54
 * @description 假期配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayConfigController {

    private final HolidayQueryService holidayQueryService;
    private final HolidayTypeMapper holidayTypeMapper;
    private final HolidayCommandService holidayCommandService;

    /**
     * 假期类型分页列表
     *
     * @param keyword 关键字查询
     * @param paging  分页对象
     *
     * @return 分页结果
     */
    @GetMapping("/holiday/page")
    public Pagination<HolidayConfigPreviewVO> list(@Nullable String keyword, Paging paging) {
        Page<HolidayTypePO> page = holidayTypeMapper.selectPage(
                PagingUtils.convert(paging),
                Wrappers.<HolidayTypePO>lambdaQuery().like(StringUtils.isNotEmpty(keyword), HolidayTypePO::getName, keyword)
                                                               );

        List<HolidayConfigPreviewVO> holidayConfigPreviewVOS = new ArrayList<>(page.getRecords().size());
        page.getRecords().forEach(holidayTypePO -> {
            // 获取最小请假单位
            String leaveRule = holidayTypePO.getLeaveRule();
            LeaveRule rule = JSONObject.parseObject(leaveRule, LeaveRule.class);
            HolidayConfigPreviewVO previewVO = HolidayConfigPreviewVO.builder().id(holidayTypePO.getId())
                    .name(holidayTypePO.getName())
                    .type(holidayTypePO.getType())
                    .leaveTimeUnit(rule.getMinimumLeaveTimeUnitConvert().getMinimumLeaveTimeUnit().name())
                    .build();

            holidayConfigPreviewVOS.add(previewVO);
        });
        return Pagination.<HolidayConfigPreviewVO>builder().pages(page.getPages()).total(page.getTotal()).records(holidayConfigPreviewVOS)
                .build();
    }

    /**
     * 查询假期类型的详情
     *
     * @param id 假期类型的id
     *
     * @return 假期类型详情
     */
    @GetMapping("/detail/id/{id}")
    public HolidayDetailDTO detail(@PathVariable("id") @NotNull Long id) {
        return holidayQueryService.getById(id);
    }

    /**
     * 查询假期类型的详情
     *
     * @param holidayType 假期类型
     *
     * @return 假期类型详情
     */
    @GetMapping("/detail/type/{type}")
    public HolidayDetailDTO detail(@PathVariable("type") @NotNull Holiday.HolidayType holidayType) {
        HolidayTypePO holidayTypePO = holidayTypeMapper.selectOne(Wrappers.<HolidayTypePO>lambdaQuery()
                                                                          .eq(HolidayTypePO::getType, holidayType));
        Assert.isNotFound(holidayTypePO, "不存在假期");
        return holidayQueryService.getById(holidayTypePO.getId());
    }

    /**
     * 根据id修改假期配置
     *
     * @param changeHolidayConfigCommand 修改配置命令
     */
    @PutMapping("/holiday/id/{id}")
    public void change(@RequestBody @Valid ChangeHolidayConfigCommand changeHolidayConfigCommand) {
        holidayCommandService.changeHolidayConfig(changeHolidayConfigCommand);
    }
}
