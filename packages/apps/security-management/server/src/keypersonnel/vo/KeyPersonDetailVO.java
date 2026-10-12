package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class KeyPersonDetailVO extends KeyPersonPageVO {
    /**
     * 身份证号。是否脱敏由字典配置控制。
     */
    private String idCard;

    /**
     * 人员照片 fileId。
     */
    private Long photoFileId;

    /**
     * 人员照片文件信息。
     */
    private FileVO photoFile;

    /**
     * 人员档案附件 fileId 列表。
     */
    private List<Long> personAttachmentFileIds;

    /**
     * 人员档案附件文件信息列表。
     */
    private List<FileVO> personAttachmentFiles;

    /**
     * 监管上报记录时间轴，默认返回第一页。
     */
    private List<KeyPersonReportVO> reports;
}
