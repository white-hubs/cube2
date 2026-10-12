package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class KeyPersonReportVO {
    /**
     * 监管上报记录主键。
     */
    private Long id;

    /**
     * 重点人员主键。
     */
    private Long personId;

    /**
     * 上报内容。
     */
    private String content;

    /**
     * 上报附件 fileId 列表。
     */
    private List<Long> reportAttachmentFileIds;

    /**
     * 上报附件文件信息列表。
     */
    private List<FileVO> reportAttachmentFiles;

    /**
     * 上报时间，服务端生成。
     */
    private LocalDateTime reportTime;

    /**
     * 上报人 ouaa 用户主键。
     */
    private Long reporterUserId;

    /**
     * 上报人姓名。
     */
    private String reporterName;
}
