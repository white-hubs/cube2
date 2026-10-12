package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardTypeReferCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectStandardQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardToTypeCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardTypeReferCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardTypeReferPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardTargetVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * <p>
 * 巡检标准 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectStandardController {

    private final InspectStandardCommandService commandService;
    private final InspectStandardQueryService queryService;
    private final InspectStandardTypeReferCommandService inspectStandardTypeReferCommandService;

    @GetMapping("/inspect-standard/{id}")
    public InspectStandardVO getInspectStandard(@PathVariable Long id) {
        return queryService.getVOById(id);
    }

    /**
     * 巡检标准分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-standard/page")
    public Pagination<InspectStandardVO> getPage(InspectStandardQueryParam param) {
        return queryService.getPage(param);
    }

    @GetMapping("/inspect-standard/list")
    public List<InspectStandardVO> getList(InspectStandardQueryParam param) {
        return queryService.getList(param);
    }

    /**
     * 根据 目标对象ID
     */
    @GetMapping("/inspect-standard/{targetCategory}/{targetId}")
    public InspectStandardTargetVO getVoByTarget(@PathVariable("targetCategory") String targetCategory,
                                                 @PathVariable("targetId") Long targetId
                                                ) {
        return queryService.getVoByTarget(targetCategory, targetId);
    }

    @GetMapping("/inspect-standard/targetCategory/{targetCategory}/targetType/{targetType}")
    public List<InspectStandardVO> getStandardsByTarget(@PathVariable("targetCategory") String targetCategory,
                                                        @PathVariable("targetType") String targetType
                                                       ) {
        return queryService.getStandardsByTargetType(targetCategory, targetType);
    }

    /**
     * 保存
     *
     * @param param
     */
    @PostMapping("/inspect-standard")
    public void save(@RequestBody @Validated InspectStandardCommandModel param) {
        commandService.save(param);
    }

    /**
     * 设置未默认巡检标准
     *
     * @param param
     */
    @PostMapping("/inspect-standard/defaultStandard")
    public void defaultStandard(@Valid @RequestBody InspectStandardTypeReferCommand param) {
        InspectStandardTypeReferPO refer = new InspectStandardTypeReferPO();
        refer.setDefaultStandard("0");
        UpdateWrapper<InspectStandardTypeReferPO> updateWrapper = new UpdateWrapper<>();
        // 先取消所有的标准都是非默认标准
        updateWrapper.eq("target_category", param.getTargetCategory());
        updateWrapper.eq("target_type", param.getTargetType());
        refer.setDefaultStandard(param.getDefaultStandard());
        updateWrapper.eq("standard_id", param.getStandardId());
        // 更新需要设置的标准
        inspectStandardTypeReferCommandService.update(null, updateWrapper.set("default_standard", param.getDefaultStandard()));
    }

    /**
     * 类型关联标准
     */
    @PostMapping("/inspect-standard/type")
    public void saveToType(@RequestBody @Validated InspectStandardToTypeCommandModel param) {
        commandService.saveToType(param);
    }

    /**
     * 标准启停用
     */
    @PostMapping("/inspect-standard/{id}/enabled")
    public void enabled(@PathVariable Long id, @RequestParam("enable") Boolean enable) {
        commandService.enabled(id, enable);
    }

    @PutMapping("/inspect-standard/{id}")
    public void update(@PathVariable Long id, @RequestBody @Validated InspectStandardCommandModel param) {
        commandService.update(id, param);
    }

    @DeleteMapping("/inspect-standard/{id}")
    public void remove(@PathVariable Long id) {
        commandService.remove(id);
    }

    @DeleteMapping("/inspect-standard/batch")
    public void removeBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") String ids) {
        commandService.removeBatch(ids);
    }
}

