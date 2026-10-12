package com.guhecloud.cube.base.device.center.core.controller;

import com.guhecloud.cube.base.device.center.core.dto.DeviceListDto;
import com.guhecloud.cube.base.device.center.core.excel.ExcelWriter;
import com.guhecloud.cube.base.device.center.core.param.ExportTemplateParam;
import com.guhecloud.cube.base.device.center.core.service.CenterDeviceBatchOptService;
import com.guhecloud.cube.base.device.center.core.service.CenterDeviceService;
import com.guhecloud.cube.base.device.center.core.vo.BatchImportExcelTemplate;
import com.guhecloud.cube.base.device.center.core.vo.BatchOptResult;
import com.guhecloud.cube.base.device.center.core.vo.DeviceIdAndCode;
import com.guhecloud.cube.base.device.center.metadata.annotation.InvokeForm;
import com.guhecloud.cube.base.device.center.metadata.lang.From;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceExpandVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceThinDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alibaba.nacos.common.http.param.MediaType.APPLICATION_OCTET_STREAM;
import static com.guhecloud.cube.base.device.center.metadata.lang.Const.DEVICE_CENTER_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
public class CenterDeviceBatchController {

    @Resource
    private IotDeviceClient iotDeviceClient;
    @Resource
    private CenterDeviceBatchOptService centerDeviceBatchOptService;
    @Resource
    private CenterDeviceService centerDeviceService;

    /**
     * 下载设备模板
     *
     * @param exportTemplateParam 参数
     * @param response            res
     *
     * @throws IOException 写文件出错
     */
    @GetMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/mgt/batch/export-template")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public void exportTemplate(ExportTemplateParam exportTemplateParam, HttpServletResponse response) throws IOException {
        List<BatchImportExcelTemplate> dataList = fetchIotDeviceInfo(exportTemplateParam);

        //write to excel
        writeTemplate(dataList, response);
    }

    /**
     * 模板导入
     *
     * @param file excel
     *
     * @return result
     *
     * @throws IOException 读文件出错
     */
    @PostMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/mgt/batch/import-form-template")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public BatchOptResult importFromTemplate(MultipartFile file) throws IOException {
        return centerDeviceBatchOptService.importFromTemplate(file.getInputStream());
    }

    /**
     * 从IOT导出设备
     *
     * @param exportTemplateParam param
     *
     * @return data
     */
    private List<BatchImportExcelTemplate> fetchIotDeviceInfo(ExportTemplateParam exportTemplateParam) {
        int pageNumber = 1;
        int pageSize = 1000;
        List<BatchImportExcelTemplate> dataList = new LinkedList<>();
        while (true) {
            //fetch iot device
            DeviceQueryParam deviceQueryParam = new DeviceQueryParam();
            deviceQueryParam.setProductId(exportTemplateParam.getProductId());
            deviceQueryParam.setPageSize(pageSize);
            deviceQueryParam.setPageNumber(pageNumber);
            deviceQueryParam.setWithNumberAndBrandPath(true);
            Pagination<DeviceThinDTO> page = iotDeviceClient.page(deviceQueryParam);

            //break if null
            if (Objects.isNull(page) || Objects.isNull(page.getRecords()) || page.getRecords().isEmpty()) {
                break;
            }

            Set<Long> deviceIds = page.getRecords().stream().map(deviceThinDTO -> Long.parseLong(deviceThinDTO.getDeviceId()))
                    .collect(Collectors.toSet());

            //get DeviceCode
            Map<String, String> deviceCodeMap = centerDeviceService.queryDeviceCodeByIds(deviceIds)
                    .stream()
                    .collect(Collectors.toMap(DeviceIdAndCode::getDeviceId, DeviceIdAndCode::getDeviceCode));

            //get DeviceExpandVO
            Map<String, DeviceExpandVO> deviceExpandMaps = centerDeviceService.selectDeviceExpand(new ArrayList<>(deviceIds)).stream()
                    .collect(Collectors.toMap(deviceExpandVO -> deviceExpandVO.getId().toString(), deviceExpandVO -> deviceExpandVO));
            //get BizDevice
            Map<String, DeviceListDto> deviceDetailMaps = centerDeviceService.queryDeviceDetailsNewList(deviceIds).stream()
                    .collect(Collectors.toMap(deviceListDto -> deviceListDto.getId().toString(), deviceListDto -> deviceListDto));

            //convert to template
            List<DeviceThinDTO> records = page.getRecords();
            for (DeviceThinDTO deviceThin : records) {

                //已存在的设备ID过滤
                if (exportTemplateParam.isFilterExistDevice() &&
                    centerDeviceService.existDeviceId(Long.parseLong(deviceThin.getDeviceId()))) {
                    continue;
                }

                BatchImportExcelTemplate template = new BatchImportExcelTemplate();
                template.setIotDeviceId(deviceThin.getDeviceId());
                template.setIotDeviceCode(deviceCodeMap.get(deviceThin.getDeviceId()));//使用设备三维模型编码
                template.setDeviceName(deviceThin.getDeviceName());

                template.setProductId(deviceThin.getProductId());
                template.setProductName(deviceThin.getProductName());
                template.setCategoryNameConcat(deviceThin.getProdCategoryNameConcat());
                template.setCustomerTag(deviceThin.getTags());
                DeviceListDto deviceListDto = deviceDetailMaps.get(deviceThin.getDeviceId());

                DeviceExpandVO deviceExpandVO = deviceExpandMaps.get(deviceThin.getDeviceId());

                // 中心库资料缺失时保留空白，避免导出模板因单条设备缺项失败
                if (Objects.nonNull(deviceListDto)) {
                    //维护部门
                    if (!ObjectUtils.isEmpty(deviceListDto.getDepartmentName())) {
                        template.setDepartmentId(deviceListDto.getDepartmentName());
                    }
                    //区域
                    template.setAreaId(deviceListDto.getAreaName());
                    //维护人员
                    template.setManagerIds(deviceListDto.getManagerNames());
                }
                if (Objects.nonNull(deviceExpandVO)) {
                    //设备位置
                    template.setLntAndLat(deviceExpandVO.getLntAndLat());
                    //采购日期
                    if (!ObjectUtils.isEmpty(deviceExpandVO.getPurchaseDate())) {
                        template.setPurchaseDate(deviceExpandVO.getPurchaseDate().toLocalDate());
                    }
                    //生产日期
                    if (!ObjectUtils.isEmpty(deviceExpandVO.getProductionDate())) {
                        template.setProductionDate(deviceExpandVO.getProductionDate().toLocalDate());
                    }
                    //采购单价
                    template.setUnitPrice(deviceExpandVO.getUnitPrice());
                    //使用寿命
                    if (!ObjectUtils.isEmpty(deviceExpandVO.getUsefulLife())) {
                        template.setUsefulLife(deviceExpandVO.getUsefulLife().toString());
                    }
                }

                dataList.add(template);
            }

            //fetch next page
            pageNumber++;
        }

        return dataList;
    }

    /**
     * 写sheet
     *
     * @param list     list
     * @param response res
     *
     * @throws IOException io
     */
    private void writeTemplate(List<BatchImportExcelTemplate> list, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_OCTET_STREAM);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=批量导入模板.xlsx");

        ExcelWriter<BatchImportExcelTemplate> excelWriter = new ExcelWriter<>();
        excelWriter.writeData(list, centerDeviceBatchOptService.buildExcelColumnSelectionProvider(), BatchImportExcelTemplate.class, response.getOutputStream(), false);
    }

}
