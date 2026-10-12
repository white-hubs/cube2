package com.guhecloud.cube.biz.work.center.server.controller;

import com.guhecloud.cube.biz.work.center.server.param.HandTakeoverParam;
import com.guhecloud.cube.biz.work.center.server.param.HandoverRecordParam;
import com.guhecloud.cube.biz.work.center.server.param.HandoverRecordQueryParam;
import com.guhecloud.cube.biz.work.center.server.service.HandoverRecordService;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordDetailVO;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作交接API
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 16:01
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/handover/record")
public class HandoverRecordController {

    private final HandoverRecordService handoverRecordService;

    /**
     * 新增交接班记录
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/add")
    public boolean addHandoverRecord(@RequestBody HandoverRecordParam param) {
        return handoverRecordService.addHandoverRecord(param);
    }

    /**
     * 接班
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/takeover")
    public void takeOver(@RequestBody HandTakeoverParam param) {
        handoverRecordService.takeOver(param);
    }

    /**
     * 撤销
     */
    @DeleteMapping("/cancel/{id}")
    public boolean cancel(@PathVariable Long id) {
        return handoverRecordService.cancel(id);
    }

    /**
     * 查询交接班记录列表
     *
     * @param param 交接班记录查询参数
     *
     * @return
     */
    @GetMapping("/list")
    public Pagination<HandoverRecordVO> getHandoverRecord(HandoverRecordQueryParam param) {
        return handoverRecordService.getHandoverRecord(param);
    }

    /**
     * 查询交接班记录详情
     *
     * @param id 交接班记录ID
     *
     * @return
     */
    @GetMapping("/detail")
    public HandoverRecordDetailVO getHandoverRecordDetail(@RequestParam("id") Long id) {
        return handoverRecordService.getHandoverRecordDetail(id);
    }

}
