package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.CommonStatParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFundInoutRecordParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFundInoutRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFundInoutRecordService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFundInoutRecordVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFundInoutStatVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 应急资金收支记录
 * @date 2024-01-22 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFundInoutRecordController {
    private final EmergencyFundInoutRecordService emergencyFundInoutRecordService;

    /**
     * 功能描述： 应急资金收支记录添加
     */
    @PostMapping(value = "/emergency-fund-inout-record")
    public Long add(@Valid @RequestBody EmergencyFundInoutRecordParam param) {
        return emergencyFundInoutRecordService.add(param);
    }

    /**
     * 功能描述： 应急资金收支记录修改
     */
    @PutMapping(value = "/emergency-fund-inout-record/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencyFundInoutRecordParam param) {
        emergencyFundInoutRecordService.update(id, param);
    }

    /**
     * 功能描述： 应急资金收支记录删除
     */
    @DeleteMapping(value = "/emergency-fund-inout-record/{id}")
    public void delete(@PathVariable Long id) {
        emergencyFundInoutRecordService.delete(id);
    }

    /**
     * 功能描述： 应急资金收支记录批量删除
     */
    @DeleteMapping(value = "/emergency-fund-inout-record/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencyFundInoutRecordService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急资金收支记录分页
     */
    @GetMapping(value = "/emergency-fund-inout-record/page")
    public Pagination<EmergencyFundInoutRecordVO> getPage(EmergencyFundInoutRecordQueryParam param) {
        return emergencyFundInoutRecordService.getPage(param);
    }

    /**
     * 功能描述： 应急资金收支记录详情
     */
    @GetMapping(value = "/emergency-fund-inout-record/{id}")
    public EmergencyFundInoutRecordVO getById(@PathVariable Long id) {
        return emergencyFundInoutRecordService.getById(id);
    }

    /**
     * 应急资金每日进出统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/emergency-fund-inout-record/stat")
    public List<EmergencyFundInoutStatVO> fundInoutStatByDate(EmergencyFundInoutRecordQueryParam param) {
        return emergencyFundInoutRecordService.fundInoutStatByDate(param);
    }

    /**
     * 应急资金趋势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/emergency-fund-inout-record/trend/stat")
    public List<EmergencyFundInoutStatVO> fundInoutTrendStatByDate(CommonStatParam param) {
        return emergencyFundInoutRecordService.fundInoutTrendStatByDate(param);
    }
}