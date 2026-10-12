package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskOptionVO {
    /**
     * 选项 ID。
     */
    private Long id;

    /**
     * 选项编码。
     */
    private String code;

    /**
     * 选项名称。
     */
    private String name;

    /**
     * 选项图标文件 ID；警示标志选项使用。
     */
    private String iconFileId;

    /**
     * 选项图标文件对象，由 iconFileId 调用文件服务解析得到。
     */
    private FileVO iconFile;

    public RiskOptionVO(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
