package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.ReadUpdateRecordParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.ReadUpdateRecordQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.ReadUpdateRecordService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ReadUpdateRecordVO;
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
 * @author lrj
 * @Description 抄表修改记录
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReadUpdateRecordController {
    private final ReadUpdateRecordService readUpdateRecordService;

    /**
     * 功能描述： 抄表修改记录添加
     */
    @PostMapping(value = "/read-update-record")
    public Long add(@Valid @RequestBody ReadUpdateRecordParam param) {
        return readUpdateRecordService.add(param);
    }

    /**
     * 功能描述： 抄表修改记录修改
     */
    @PutMapping(value = "/read-update-record/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody ReadUpdateRecordParam param) {
        readUpdateRecordService.update(id, param);
    }

    /**
     * 功能描述： 抄表修改记录删除
     */
    @DeleteMapping(value = "/read-update-record/{id}")
    public void delete(@PathVariable Long id) {
        readUpdateRecordService.delete(id);
    }

    /**
     * 功能描述： 抄表修改记录批量删除
     */
    @DeleteMapping(value = "/read-update-record/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        readUpdateRecordService.batchDelete(ids);
    }

    /**
     * 功能描述： 抄表修改记录分页
     */
    @GetMapping(value = "/read-update-record/page")
    public Pagination<ReadUpdateRecordVO> getPage(ReadUpdateRecordQueryParam param) {
        return readUpdateRecordService.getPage(param);
    }

    /**
     * 功能描述： 抄表修改记录详情
     */
    @GetMapping(value = "/read-update-record/{id}")
    public ReadUpdateRecordVO getById(@PathVariable Long id) {
        return readUpdateRecordService.getById(id);
    }

}