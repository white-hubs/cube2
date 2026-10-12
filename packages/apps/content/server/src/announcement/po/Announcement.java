package com.guhecloud.cube.srv.gen.content.management.server.announcement.po;

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
public class Announcement extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知类别
     */
    private Long type;

    /**
     * 通知级别
     */
    private String level;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知时间
     */
    private LocalDateTime noticeTime;
    /**
     * 消息id
     */
    private String msgIds;
    /**
     * 是否查看
     */
    private Integer viewed;
    /**
     * 状态
     */
    private String status;

    /**
     * 是否置顶
     */
    private Integer isTopping;

    /**
     * 发布机构
     */
    private Long publishAgency;

    /**
     * 公告标签
     */
    private Long tagId;
}
