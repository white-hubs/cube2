package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 来货报备新增信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PorkReportModifyParam {
    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 档位主键
     */
    @NotNull(message = "档位不能为空")
    @Min(value = 1, message = "档位主键不能小于1")
    private Long stallId;
    /**
     * 屠宰场主键
     */
    @Min(value = 1, message = "屠宰场主键不能小于1")
    private Long abattoirId;
    /**
     * 报备片数
     */
    @NotNull(message = "报备片数不能为空")
    @Min(value = 1, message = "报备片数不能小于1")
    private Integer reportSlices;
    /**
     * 报备重量
     */
    @NotNull(message = "报备重量不能为空")
    @Min(value = 1, message = "报备重量不能小于1")
    private Double reportWeight;
    /**
     * 报备时间
     */
    @NotNull(message = "报备时间不能为空")
    private LocalDateTime reportTime;

    /**
     * 商品主键
     */
    @NotNull(message = "商品不能为空")
    private List<String> goodsCategoryIds;

    /**
     * 报备商品名称
     */
    @Size(max = 50, message = "报备商品名称不能大于50")
    @NotEmpty(message = "报备商品名称不能为空")
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 到达时间
     */
    @NotNull(message = "到达时间不能为空")
    private LocalDateTime arrivedTime;
    /**
     * 车辆号码
     */
    @NotEmpty(message = "车辆号码不能为空")
    @Size(max = 100, message = "车辆号码不能大于100")
    private String vehiclePlate;
    /**
     * 检疫证号
     */
    @NotEmpty(message = "检疫证号不能为空")
    private List<String> pratiqueCodes;

    /**
     * 产地名称
     */
    @Size(max = 100, message = "产品不能大于100")
    private String originPlace;

    /**
     * 检疫证明图片主键集合
     */
    @NotEmpty(message = "检疫证明不能为空")
    private List<Long> fileIds;

}
