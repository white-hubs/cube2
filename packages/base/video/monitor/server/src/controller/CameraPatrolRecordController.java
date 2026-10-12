package com.guhecloud.cube.biz.vmonitor.server.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecord;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolSnapCapture;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordService;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordExportVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordPageVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolSnapCaptureVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

/**
 * 视频巡逻记录接口
 *
 * @author lanchun
 * @date 2021/12/6 19:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolRecordController {

    private final CameraPatrolRecordService cameraPatrolRecordService;

    /**
     * 异常截图
     */
    @PostMapping("/camera-patrol-record/snap-capture")
    public CameraPatrolSnapCaptureVO snapCapture(@RequestBody CameraPatrolSnapCapture param) {
        return cameraPatrolRecordService.snapCapture(param);
    }

    /**
     * 视频巡逻记录添加
     *
     * @param param
     *
     * @return
     */
    @PostMapping(value = "/camera-patrol-record")
    public Long save(@Valid @RequestBody CameraPatrolRecordModifyParam param) {
        return cameraPatrolRecordService.save(param);
    }

    /**
     * 视频巡逻记录详情
     *
     * @param id 视频巡逻记录主键
     *
     * @return
     */
    @GetMapping("/camera-patrol-record/{id}")
    public CameraPatrolRecordVO getCameraPatrolRecord(@PathVariable("id") @Min(value = 1, message = "巡逻记录主键不能为空") Long id) {
        CameraPatrolRecord cameraPatrolRecord = this.cameraPatrolRecordService.getById(id);
        Assert.isNotFound(cameraPatrolRecord, "巡逻记录不存在");
        CameraPatrolRecordVO cameraPatrolRecordVO = ObjectUtils.copying(cameraPatrolRecord, CameraPatrolRecordVO.class);
        return cameraPatrolRecordVO;
    }

    /**
     * 视频巡逻记录分页列表
     *
     * @param param 查询参数
     *
     * @return
     */
    @GetMapping("/camera-patrol-records")
    public Pagination<CameraPatrolRecordPageVO> listCameraPatrolRecords(@Valid CameraPatrolRecordQueryParam param) {
        Page<CameraPatrolRecord> page = this.cameraPatrolRecordService.page(param);
        List<CameraPatrolRecordPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), CameraPatrolRecordPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 视频巡逻记录删除
     *
     * @param id 巡逻记录主键
     */
    @DeleteMapping("/camera-patrol-record/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "巡逻记录主键不能为空") Long id) {
        this.cameraPatrolRecordService.remove(id);
    }

    /**
     * 巡逻记录导出
     *
     * @param param
     *
     * @throws IOException
     */
    @PostMapping("/camera-patrol-records/export")
    public void exportPatrolLineStatistics(@Valid @RequestBody CameraPatrolRecordQueryParam param) throws IOException {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = URLEncoder.encode("巡逻记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        List<CameraPatrolRecord> cameraPatrolRecords = this.cameraPatrolRecordService.getList(param);
        List<CameraPatrolRecordExportVO> cameraPatrolRecordExportVOS = ObjectUtils.copying(cameraPatrolRecords, CameraPatrolRecordExportVO.class);
        EasyExcel.write(response.getOutputStream(), CameraPatrolRecordExportVO.class).sheet("巡逻记录")
                .doWrite(cameraPatrolRecordExportVOS);
    }
}