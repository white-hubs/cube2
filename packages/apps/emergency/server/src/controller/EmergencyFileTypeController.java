package com.guhecloud.cube.biz.emergency.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFileType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileTypeModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileTypeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileTypeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
 * 预案文件类型管理接口
 *
 * @author lanchun
 * @date 2024/5/16 15:26
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileTypeController {

    private final EmergencyFileTypeService emergencyFileTypeService;

    /**
     * 预案文件类型新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/emergency-file-type")
    public Long save(@Valid @RequestBody EmergencyFileTypeModifyParam param) {
        return this.emergencyFileTypeService.save(param);
    }

    /**
     * 预案文件类型修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/emergency-file-type/{id}")
    public void update(@Min(value = 1, message = "预案文件类型主键不能小于1") @PathVariable("id") Long id,
                       @Valid @RequestBody EmergencyFileTypeModifyParam param
                      ) {
        this.emergencyFileTypeService.update(id, param);
    }

    /**
     * 预案文件类型详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/emergency-file-type/{id}")
    public EmergencyFileTypeVO getEmergencyFile(@Min(value = 1, message = "预案文件类型主键不能小于1") @PathVariable("id") Long id) {
        EmergencyFileType emergencyFileType = this.emergencyFileTypeService.getById(id);
        return ObjectUtils.copying(emergencyFileType, EmergencyFileTypeVO.class);
    }

    /**
     * 预案文件类型分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/emergency-file-type-page")
    public Pagination<EmergencyFileTypeVO> getPage(@Valid EmergencyFileTypeQueryParam param) {
        Page<EmergencyFileType> emergencyFileTypePage = this.emergencyFileTypeService.page(param);
        List<EmergencyFileTypeVO> emergencyFileTypes = ObjectUtils.copying(emergencyFileTypePage.getRecords(), EmergencyFileTypeVO.class);
        if (CollectionUtils.isEmpty(emergencyFileTypes)) {
            return Pagination.empty();
        }
        return PagingUtils.convert(emergencyFileTypePage, emergencyFileTypes);
    }

    /**
     * 预案文件类型列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/emergency-file-types")
    public List<EmergencyFileTypeVO> getList(@Valid EmergencyFileTypeQueryParam param) {
        List<EmergencyFileType> fileTypeList = this.emergencyFileTypeService.list(param);
        return ObjectUtils.copying(fileTypeList, EmergencyFileTypeVO.class);
    }

    /**
     * 预案文件类型删除
     *
     * @param id
     */
    @DeleteMapping("/emergency-file-type/{id}")
    public void remove(@Min(value = 1, message = "预案文件类型主键不能小于1") @PathVariable("id") Long id) {
        this.emergencyFileTypeService.remove(id);
    }
}
