package com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.interfaces.vo.NoticeConfigVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Data
public class AnnouncementVO {

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
     * 通知级别名称
     */
    private String levelName;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime noticeTime;

    /**
     * 创建用户
     */
    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private String typeName;

    /**
     * 是否查看
     */
    private Boolean viewed;

    /**
     * 发布机构
     */
    private String publishAgency;
    /**
     * 公告标签
     */
    private String tagName;
    /**
     * 是否置顶
     */
    private String isTopping;
    /**
     * 是否隐藏
     */
    private String status;

    /**
     * 一级类型
     */
    private String firstType;
    /**
     * 消息配置信息
     */
    private List<NoticeConfigVO> noticeConfigs;

    //附件
    private List<FileVO> files = new ArrayList<>();
}
