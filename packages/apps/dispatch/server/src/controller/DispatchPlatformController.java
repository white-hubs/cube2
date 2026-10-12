package com.guhecloud.cube.biz.dispatch.server.controller;

import com.guhecloud.cube.biz.dispatch.server.model.DispatchCallQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchFileQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGPSQueryModel;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchCallQueryParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchFileQueryParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchGPSQueryParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchLoginParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchCallRecordVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchCommonTypeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceStatusVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchFileVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGPSVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 调度台
 * @date 2022-07-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchPlatformController {
    private final DispatchService dispatchService;

    /**
     * 功能描述： 登陆并返回token
     */
    @PostMapping(value = "/dispatch-platform/login")
    public String dispatchLogin(@Valid @RequestBody DispatchLoginParam param) {
        return dispatchService.dispatchLogin(param.getUserName(), param.getPassword());
    }

    /**
     * 功能描述： 调度呼叫记录分页
     */
    @GetMapping(value = "/dispatch-platform/call/page")
    public Pagination<DispatchCallRecordVO> getPageForCall(DispatchCallQueryParam param) {
        DispatchCallQueryModel query = DispatchCallQueryModel.builder()
                .code(param.getCode())
                .callerNumber(param.getCallerNumber())
                .calleeNumber(param.getCalleeNumber())
                .callType(param.getCallType())
                .callResult(param.getCallResult())
                .pageNumber(param.getPageNumber())
                .pageSize(param.getPageSize())
                .build();
        if (param.getPeriod() != null) {
            query.setStartTime(dateFormat(param.getPeriod().getBegin()));
            query.setEndTime(dateFormat(param.getPeriod().getEnd()));
        }
        return dispatchService.callPage(query);
    }

    /**
     * 功能描述： 调度过程中产生的文件列表
     */
    @GetMapping(value = "/dispatch-platform/file/list")
    public List<DispatchFileVO> getListForFile(@Valid DispatchFileQueryParam param) {
        DispatchFileQueryModel query = DispatchFileQueryModel.builder().code(param.getCode()).build();
        if (param.getPeriod() != null) {
            query.setStartTime(dateFormat(param.getPeriod().getBegin()));
            query.setEndTime(dateFormat(param.getPeriod().getEnd()));
        }
        return dispatchService.communicationFile(query);
    }

    @GetMapping(value = "/dispatch-platform/file/page")
    public Pagination<DispatchFileVO> getPageForFile(@Valid DispatchFileQueryParam param) {
        DispatchFileQueryModel query = ObjectUtils.copying(param, DispatchFileQueryModel.class);
        if (param.getPeriod() != null) {
            query.setStartTime(dateFormat(param.getPeriod().getBegin()));
            query.setEndTime(dateFormat(param.getPeriod().getEnd()));
        }
        return dispatchService.communicationFilePage(query);
    }

    /**
     * 功能描述： gps历史轨迹查询
     */
    @GetMapping(value = "/dispatch-platform/gps/list")
    public List<DispatchGPSVO> getListForGPS(@Valid DispatchGPSQueryParam param) {
        DispatchGPSQueryModel query = DispatchGPSQueryModel.builder()
                .code(param.getCode())
                .pageNumber(param.getPageNumber())
                .pageSize(param.getPageSize())
                .build();
        if (param.getPeriod() != null) {
            query.setStartTime(dateFormat(param.getPeriod().getBegin()));
            query.setEndTime(dateFormat(param.getPeriod().getEnd()));
        }
        return dispatchService.gpsHistory(query);
    }

    @GetMapping(value = "/dispatch-platform/gps/history/page")
    public Pagination<DispatchGPSVO> getPageForGPS(@Valid DispatchGPSQueryParam param) {
        DispatchGPSQueryModel query = DispatchGPSQueryModel.builder()
                .code(param.getCode())
                .pageNumber(param.getPageNumber())
                .pageSize(param.getPageSize())
                .build();
        if (param.getPeriod() != null) {
            query.setStartTime(dateFormat(param.getPeriod().getBegin()));
            query.setEndTime(dateFormat(param.getPeriod().getEnd()));
        }
        return dispatchService.gpsHistoryPage(query);
    }

    /**
     * 功能描述： 查询多个设备的状态
     */
    @GetMapping(value = "/dispatch-platform/device/status")
    public List<DispatchDeviceStatusVO> getDeviceStatus(@RequestParam List<String> codes) {
        return dispatchService.deviceStatus(codes);
    }

    /**
     * 功能描述： 获取所有的设备（终端）类型
     */
    @GetMapping(value = "/dispatch-platform/device/type")
    public List<DispatchCommonTypeVO> getDeviceType() {
        return dispatchService.getAllDeviceType();
    }

    /**
     * 功能描述： 获取所有的组类型
     */
    @GetMapping(value = "/dispatch-platform/group/type")
    public List<DispatchCommonTypeVO> getGroupType() {
        return dispatchService.getAllGroupType();
    }

    /**
     * 获取通话类型
     *
     * @return
     */
    @GetMapping(value = "/dispatch-platform/call/type")
    public List<DispatchCommonTypeVO> getCallType() {
        return dispatchService.getCallType();
    }

    /**
     * 处理时间  当没有时分秒的时候，拼接上时分秒
     *
     * @param date
     *
     * @return
     */
    private String dateFormat(LocalDateTime date) {
        return DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }

}