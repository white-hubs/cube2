package com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2022/5/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TechnicalNotice extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 通报标题
     */
    private String title;

    /**
     * 通报说明
     */
    private String illustrate;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 通报时间
     */
    private LocalDateTime noticeTime;

    /**
     * 创建者用户主键
     */
    private Long userId;

    /**
     * 创建者
     */
    private String userName;
}
