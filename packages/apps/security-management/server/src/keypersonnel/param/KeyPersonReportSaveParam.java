package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class KeyPersonReportSaveParam {
    /**
     * 上报内容，最多 1000 字。
     */
    @Size(max = 1000, message = "上报内容不能大于1000")
    private String content;

    /**
     * 上报附件 fileId 列表，文件内容由 base/file 管理。
     */
    private List<Long> reportAttachmentFileIds;
}
