package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.enumeration.QrcodeEnum;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.List;

/**
 * @author bq
 * @date 2022/7/22
 * @description 二维码
 */
@Data
public class QrcodeVO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 实体类型
     */
    private String entityType;

    /**
     * 实体类型名称
     */
    private String entityTypeName;

    /**
     * 实体ID
     */
    private String entityId;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 样式ID
     */
    private String styleId;

    /**
     * 样式配置Json
     */
    private String configJson;

    List<QrcodeThirdLinkVO> thirdLinks;

    public void setEntityType(String entityType) {
        this.entityType = entityType;
        if (StringUtils.isNotEmpty(entityType)) {
            this.entityTypeName = QrcodeEnum.valueOf(entityType).getDescription();
        }
    }
}