package com.guhecloud.cube.base.device.center.metadata.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2024/1/10 10:36
 * @description 产品信息
 */
@Data
public class DeviceProductInfoDTO {
    /**
     * 产品id
     */
    private Long id;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品图片
     */
    private Long picture;
    /**
     * 产品图标
     */
    private Long icon;
    /**
     * 产品型号名称
     */
    private String modelName;
    /**
     * 产品品牌名称
     */
    private String brandName;
    /**
     * 产品品类信息
     */
    private ProductCategory productCategory;

    /**
     * 设备图片
     */
    private FileVO image;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductCategory {
        /**
         * 产品品类名称
         */
        private String name;
        /**
         * 产品品类编号
         */
        private String code;
        /**
         * 全路径code
         */
        private String pathCode;

        public ProductCategory(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }
}
