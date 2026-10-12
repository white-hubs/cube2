package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EnterpriseInterviewVO extends BasicTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 走访时间
     */
    private LocalDateTime time;

    /**
     * 走访类型ID
     */
    private Long interviewTypeId;

    /**
     * 走访类型名称
     */
    private String interviewTypeName;

    /**
     * 走访人员
     */
    private String person;

    /**
     * 企业接待人员
     */
    private String receptionPerson;

    /**
     * 走访记录
     */
    private String recordContent;

    /**
     * 关联诉求ID
     */
    private String appealIds;

    /**
     * 关联诉求信息
     */
    private List<EnterpriseAppealListVO> appealListVOS;

    /**
     * 文件信息
     */
    private List<FileVO> fileVOS;

}
