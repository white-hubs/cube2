package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EnterpriseAppealVO extends BasicTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 诉求类型ID
     */
    private Long appealTypeId;

    /**
     * 诉求类型名
     */
    private String appealTypeName;

    /**
     * 诉求时间
     */
    private LocalDateTime time;

    /**
     * 反馈人员名
     */
    private String username;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 描述
     */
    private String description;

    /**
     * 解决措施
     */
    private String measure;

    /**
     * 文件信息
     */
    private List<FileVO> fileVOS;

}
