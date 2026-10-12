package com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/9 10:35
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicAnnouncementVO1 {
    private Long id;
    private String title;
    /**
     * 发布机构
     */
    private String publishAgency;
    /**
     * 公告标签
     */
    private String tagName;

    /**
     * 公告类别
     */
    private String typeName;
    /**
     * 通知级别
     */
    private String level;
    /**
     * 通知级别名称
     */
    private String levelName;
    /**
     * 发布时间
     */
    private LocalDateTime createTime;
    private String createBy;
    /**
     * 内容
     */
    private String content;
    private List<FileVO> fileVOS;
}
