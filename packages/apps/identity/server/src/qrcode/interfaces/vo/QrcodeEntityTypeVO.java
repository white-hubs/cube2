package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 二维码实体类型
 * @date 2023/11/20 14:37
 */
@Data
public class QrcodeEntityTypeVO {
    private Long id;
    /**
     * 实体名称
     */
    private String typeName;
    /**
     * 实体类型
     */
    private String entityType;
    /**
     * 实体关联样式id
     */
    private Long styleId;
    /**
     * 二维码实体属性
     */
    private List<QrcodeEntityAttribute> attributeVOS;

    /**
     * @Author wql
     * @Description 二维码实体属性
     * @Date 14:39 2023/11/20
     **/
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class QrcodeEntityAttribute {
        /**
         * 编号
         */
        private String code;

        /**
         * 名称
         */
        private String name;
    }
}
