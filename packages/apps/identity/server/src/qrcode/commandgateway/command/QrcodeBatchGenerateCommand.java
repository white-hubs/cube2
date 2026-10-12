package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 二维码批量生成
 * @date 2023/11/20 16:03
 */
@Data
public class QrcodeBatchGenerateCommand {

    /**
     * 实体类型
     */
    @NotEmpty(message = "实体类型不能为空")
    private String entityType;

    /**
     * 实体ID
     */
    @NotEmpty(message = "实体对象id不能为空")
    private List<EntityInfoCommand> entityInfo;

    /**
     * 样式ID
     */
    private Long styleId;

    private QrcodeStylePO stylePO;

    /**
     * 二维码ID集合。用于批量下载已存在的二维码，避免影响原批量生成接口入参。
     */
    private List<Long> qrcodeIds;

}
