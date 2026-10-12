package com.guhecloud.cube.biz.emergency.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileExpirationStatusEnum;
import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileLevelEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预案文件返回参数
 *
 * @author lanchun
 * @date 2022/9/1 17:26
 */
@Data
public class EmergencyFileVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 文件类型主键
     */
    private Long emergencyFileTypeId;

    /**
     * 文件类型名称
     */
    private String emergencyFileTypeName;

    /**
     * 级别
     */
    private EmergencyFileLevelEnum levelCode;

    /**
     * 到期状态
     */
    private EmergencyFileExpirationStatusEnum expirationStatus;

    /**
     * 到期时间
     */
    private LocalDateTime expirationDate;

    /**
     * 预案事件分类编号
     */
    private String emergencyEventTypeCode;

    /**
     * 预案事件分类名称
     */
    private String emergencyEventTypeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文档附件
     */
    private String documentFileId;

    /**
     * 文档附件对象信息
     */
    private List<FileVO> documentFileVOS;

    /**
     * 流程附件
     */
    private String processFileId;

    /**
     * 流程附件对象信息
     */
    private List<FileVO> processFileVOS;
}
