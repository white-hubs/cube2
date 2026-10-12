package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.guhecloud.cube.base.iot.product.model.v3.param.ProductModelExportParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductModelImportParam;

import java.util.Map;

/**
 * 物模型导入导出Service接口
 * 负责物模型的导出(JSON格式)和导入功能
 */
public interface V3ProductModelImportExportService {

    /**
     * 导出物模型
     * 生成自描述的JSON格式,使用业务code而非ID,便于跨环境迁移
     *
     * @param param 导出参数
     * @return 物模型JSON字符串
     */
    String exportProductModel(ProductModelExportParam param);

    /**
     * 导入物模型
     * 解析JSON内容,通过code匹配相关实体,导入物模型数据
     *
     * @param param 导入参数
     * @return 导入结果统计
     */
    Map<String, Object> importProductModel(ProductModelImportParam param);

    /**
     * 导入验证
     * 检查必填字段和引用完整性,不实际导入数据
     *
     * @param param 导入参数
     * @return 验证结果
     */
    Map<String, Object> validateImport(ProductModelImportParam param);
}
