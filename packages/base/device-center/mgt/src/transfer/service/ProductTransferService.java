package com.guhecloud.cube.iot.mgt.transfer.service;

import com.guhecloud.cube.iot.mgt.transfer.dto.excel.ProductExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.param.ProductExportParam;
import com.guhecloud.cube.iot.mgt.transfer.dto.result.ExportResult;
import com.guhecloud.cube.iot.mgt.transfer.dto.result.ImportResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

/**
 * 产品传输服务接口
 * 每次导入导出处理单个产品信息
 */
public interface ProductTransferService {

    /**
     * 导出单个产品信息到Excel（包含产品基础信息、属性、事件、命令）
     *
     * @param param        导出参数
     * @param outputStream 输出流
     *
     * @return 导出结果
     */
    ExportResult exportProductToExcel(ProductExportParam param, OutputStream outputStream);

    /**
     * 导出单个产品信息到JSON（包含产品基础信息、属性、事件、命令）
     *
     * @param param        导出参数
     * @param outputStream 输出流
     *
     * @return 导出结果
     */
    ExportResult exportProductToJson(ProductExportParam param, OutputStream outputStream);

    /**
     * 导入单个产品信息（包含产品基础信息、属性、事件、命令）
     *
     * @param file 上传文件
     *
     * @return 导入结果
     */
    ImportResult importProductFromFile(MultipartFile file);

    /**
     * 获取单个产品的Excel数据（包含产品基础信息、属性、事件、命令）
     *
     * @param param 导出参数
     *
     * @return 产品Excel数据
     */
    ProductExcelDto getProductExcelData(ProductExportParam param);
}