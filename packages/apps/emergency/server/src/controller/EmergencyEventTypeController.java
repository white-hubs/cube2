package com.guhecloud.cube.biz.emergency.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyEventType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyEventTypeModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyEventTypeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyEventTypeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyEventTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预案事件分类
 *
 * @author lanchun
 * @date 2023/9/12 14:41
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyEventTypeController {

    private final EmergencyEventTypeService emergencyEventTypeService;

    /**
     * 事件分类分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/emergency-event-type/page")
    public Pagination<EmergencyEventTypeVO> getPage(@Valid EmergencyEventTypeQueryParam query) {
        Page<EmergencyEventType> page = this.emergencyEventTypeService.getPage(query);
        return PagingUtils.convert(page, EmergencyEventTypeVO.class);
    }

    /**
     * 事件分类列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/emergency-event-types")
    public List<EmergencyEventTypeVO> getList(@Valid EmergencyEventTypeQueryParam query) {
        List<EmergencyEventType> emergencyEventTypes = this.emergencyEventTypeService.getList(query);
        return ObjectUtils.copying(emergencyEventTypes, EmergencyEventTypeVO.class);
    }

    /**
     * 事件分类新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/emergency-event-type")
    public Long save(@Valid @RequestBody EmergencyEventTypeModifyParam param) {
        return this.emergencyEventTypeService.save(param);
    }

    /**
     * 事件分类修改
     *
     * @param param
     */
    @PutMapping("/emergency-event-type/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody EmergencyEventTypeModifyParam param) {
        this.emergencyEventTypeService.update(id, param);
    }

    /**
     * 事件分类删除
     *
     * @param id
     */
    @DeleteMapping("/emergency-event-type/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.emergencyEventTypeService.remove(id);
    }
}
