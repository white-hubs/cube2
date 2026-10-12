package com.guhecloud.cube.srv.gen.safety.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmTypeQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmTypeVO;
import com.guhecloud.cube.srv.gen.safety.management.po.AlarmTypePO;
import com.guhecloud.cube.srv.gen.safety.management.service.AlarmTypeService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 报警分类
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmTypeController {

    private final AlarmTypeService alarmTypeService;

    /**
     * 报警分类分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/alarm-type/page")
    public Pagination<AlarmTypeVO> getAlarmTypePage(@Valid AlarmTypeQuery query) {
        Page<AlarmTypePO> page = this.alarmTypeService.getPage(query);
        return PagingUtils.convert(page, AlarmTypeVO.class);
    }

    /**
     * 报警分类列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/alarm-types")
    public List<AlarmTypeVO> getAlarmTypes(@Valid AlarmTypeQuery query) {
        List<AlarmTypePO> alarmTypePOS = this.alarmTypeService.getAlarmTypes(query);
        return ObjectUtils.copying(alarmTypePOS, AlarmTypeVO.class);
    }
}
