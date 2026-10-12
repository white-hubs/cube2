package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionBasicPageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionBasicParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.StandardTreeQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionBasicDetailVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionBasicVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.StandardTreeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import jakarta.validation.Valid;
import lombok.NonNull;
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
 * 医疗检测
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DetectionController {
    private final IDetectionService detectionService;

    /**
     * 新增依据信息
     *
     * @param basicParam 新增信息
     *
     * @return 成功返回true
     */
    @PostMapping("/detection/basic")
    public Boolean insertBasicDetection(@RequestBody @Valid DetectionBasicParam basicParam) {
        log.info("新增检测依据，请求参数：{}", basicParam);
        //校验请求参数
        validBasicParam(basicParam);
        //新增数据
        detectionService.insertDetectionBasic(basicParam, IdWorker.getId());
        return true;
    }

    /**
     * 删除依据信息
     *
     * @param id 依据id
     *
     * @return 成功返回true
     */
    @DeleteMapping("/detection/basic/{id}")
    public Boolean delBasicDetection(@PathVariable Long id) {
        log.info("删除检测依据，请求参数：{}", id);
        //新增数据
        detectionService.deleteDetectionBasic(id);
        return true;
    }

    /**
     * 修改依据信息
     *
     * @param basicParam 依据修改参数
     *
     * @return 返回信息
     */
    @PutMapping("/detection/basic")
    public Boolean updateBasicDetection(@RequestBody @Valid DetectionBasicParam basicParam) {
        Long id = basicParam.getId();
        log.info("修改检测依据，请求参数id：{},param:{}", id, basicParam);
        //新增数据
        detectionService.updateDetectionBasic(id, basicParam);
        return true;
    }

    /**
     * 检测依据分页
     *
     * @return 分页信息
     */
    @GetMapping("/detection/page")
    public Pagination<DetectionBasicVO> pageBasicDetection(DetectionBasicPageParam detectionBasicPageParam) {
        log.info("检测依据分页查询，请求参数：{}", detectionBasicPageParam);
        //新增数据
        return detectionService.detectionBasicPage(detectionBasicPageParam.getPageNumber(), detectionBasicPageParam.getPageSize(), detectionBasicPageParam.getKeyword());
    }

    /**
     * 检测依据详情
     *
     * @param id 依据id
     *
     * @return 分页信息
     */
    @GetMapping("/detection/{id}")
    public DetectionBasicDetailVO getBasicDetectionById(@PathVariable Long id) {
        log.info("检测依据详情，请求参数：{}", id);
        //新增数据
        return detectionService.getDetectionBasicById(id);
    }

    /**
     * 下载报告检测数据源列表
     */
    @GetMapping("/detection/download-excel-source-list/{recordId}")
    public void downloadSourceList(@PathVariable Long recordId) {
        log.info("检测报告数据源列表下载，请求参数:{}", recordId);
        detectionService.downloadExcelSourceList(recordId);
    }

    /**
     * 获取标准树形集合
     *
     * @param param 产品id集合
     *
     * @return 标准数形集合
     */
    @PostMapping("/detection/standard-tree")
    public List<StandardTreeVO> getStandardTree(@RequestBody StandardTreeQueryParam param) {
        return detectionService.getStandardTree(param);
    }

    private void validBasicParam(@NonNull DetectionBasicParam basicParam) {
        List<DetectionBasicParam.StandardParam> standardParams = basicParam.getStandards();
        Assert.isParameterError(ListUtils.isEmpty(standardParams), "依据的标准列表为空");
        standardParams.forEach(standardParam -> {
            List<DetectionBasicParam.PropertyParam> propertyParams = standardParam.getProperties();
            Assert.isParameterError(ListUtils.isEmpty(propertyParams), "标准的绑定属性为空");
        });

    }

}
