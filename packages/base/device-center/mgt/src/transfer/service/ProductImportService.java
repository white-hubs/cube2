package com.guhecloud.cube.iot.mgt.transfer.service;

import com.guhecloud.cube.iot.mgt.transfer.dto.excel.ProductExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.result.ImportResult;

/**
 * 产品导入服务接口
 * 
 * 负责处理产品数据的导入和保存逻辑，包括：
 * - 产品基本信息的幂等性处理
 * - 自动创建不存在的品类、品牌、协议、单位
 * - 产品属性、事件的保存
 */
public interface ProductImportService {

    /**
     * 保存产品Excel数据
     * <p>
     * 该方法实现完整的产品数据导入逻辑，包括：
     * 1. 产品信息的幂等性处理（通过产品编码或名称判断是否已存在）
     * 2. 自动创建不存在的依赖项（品类、品牌、协议、单位）
     * 3. 保存或更新产品属性、事件信息
     *
     * @param excelDto 从Excel导入的产品数据
     * @return
     * @throws RuntimeException 当保存过程中发生错误时抛出异常
     */
    ImportResult saveProductExcelData(ProductExcelDto excelDto);
}