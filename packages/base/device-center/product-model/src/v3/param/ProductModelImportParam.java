package com.guhecloud.cube.base.iot.product.model.v3.param;

import com.guhecloud.cube.base.iot.product.model.v3.dto.ProductModelSnapshotDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 物模型导入参数
 */
@Data
public class ProductModelImportParam {

    /**
     * 导入模式: merge合并 replace替换
     * merge: 保留现有属性/事件/方法,仅添加新的或更新已存在的
     * replace: 删除所有现有属性/事件/方法,完全替换为导入的内容
     */
    @NotNull(message = "导入模式不能为空")
    private String importMode;

    /**
     * 物模型JSON内容
     * 格式:
     * {
     *   "product": {...},
     *   "properties": [...],
     *   "events": [...],
     *   "methods": [...]
     * }
     */
    @NotNull(message = "物模型内容不能为空")
    private ProductModelSnapshotDTO modelJson;

    /**
     * 是否仅验证不导入(预校验)
     */
    private Boolean validateOnly;

    /**
     * 是否跳过重复项
     * true: 遇到已存在的编码时跳过
     * false: 遇到已存在的编码时报错
     */
    private Boolean skipDuplicate;

    /**
     * 是否自动创建版本快照
     */
    private Boolean createSnapshot;
}
