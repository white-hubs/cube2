package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command;

import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author bq
 * @date 2022/7/20 13:30
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QrcodeCommand {

    private Long id;

    /**
     * 编号
     */
    @NotEmpty(message = "二维码code不能为空!")
    private String code;

    /**
     * 名称
     */
    @NotEmpty(message = "二维码名称不能为空!")
    private String name;

    /**
     * 实体类型
     */
    private String entityType;

    /**
     * 实体ID字符串
     */
    private String entityIdStr;

    /**
     * 实体ID
     */
    private Long entityId;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 样式ID字符串
     */
    @NotEmpty(message = "必须选择一个样式")
    private String styleIdStr;

    /**
     * 第三方链接
     */
    List<QrcodeThirdLinkCommand> thirdLinks;

    /**
     * 样式ID
     */
    private Long styleId;

    public void setEntityIdStr(String entityIdStr) {
        this.entityIdStr = entityIdStr;
        if (StringUtils.isNotEmpty(entityIdStr)) {
            this.entityId = Long.valueOf(entityIdStr);
        }
    }

    public void setStyleIdStr(String styleIdStr) {
        this.styleIdStr = styleIdStr;
        if (StringUtils.isNotEmpty(styleIdStr)) {
            this.styleId = Long.valueOf(styleIdStr);
        }
    }

}
