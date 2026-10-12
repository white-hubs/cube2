package com.guhecloud.cube.base.device.center.scene.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.AreaBaseInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.biz.mdata.client.dto.AreaBimListDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 可视化设备列表返回参数
 *
 * @author lanchun
 * @date 2024/1/15 14:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceVisualCollectionVO {

    private Long id;
    /**
     * 设备实例编码
     */
    private String code;
    /**
     * 设备实例名称
     */
    private String name;

    /**
     * 产品品牌信息
     */
    private BrandVO brand;

    /**
     * 设备扩展信息
     */
    private ExpandVO expand;

    /**
     * 产品品类信息
     */
    private ProdCategoryVO prodCategory;

    /**
     * 产品信息
     */
    private ProductVO product;

    /**
     * 部门信息
     */
    private Pair<Long, String> department;

    /**
     * 区域信息
     */
    private AreaBaseInfoDTO area;

    /**
     * 模型区域信息
     */
    private AreaBimListDTO areaBimListVO;

    /**
     * 设备状态信息
     */
    private StatusVO status;

    /**
     * 健康值
     */
    private Integer healthValue;

    /**
     * 设备状态信息
     */
    @Data
    public static class StatusVO {

        /**
         * 设备状态
         */
        private boolean onlineState;

        /**
         * 最近更新时间
         */
        private LocalDateTime communicationTime;
    }

    /**
     * 产品品类信息
     */
    @Data
    public static class ProdCategoryVO {

        /**
         * 编号
         */
        private String code;

        /**
         * 名称
         */
        private String name;
    }

    /**
     * 产品信息
     */
    @Data
    public static class ProductVO {

        /**
         * 产品名称
         */
        private Long id;

        /**
         * 产品名称
         */
        private String name;

        /**
         * 型号名称
         */
        private String modelName;
    }

    /**
     * 设备扩展信息
     */
    @Data
    public static class ExpandVO {

        /**
         * 安装时间
         */
        private LocalDateTime installDate;

        /**
         * 采购时间
         */
        private LocalDate purchaseDate;
    }

    /**
     * 产品品牌信息
     */
    @Data
    public static class BrandVO {

        /**
         * 品牌名称
         */
        private String brandName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceVisualCollectionVO that = (DeviceVisualCollectionVO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
