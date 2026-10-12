package com.guhecloud.cube.base.videogw.server.common.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 视频文件信息
 *
 * @author lanchun
 * @date 2024/11/28 14:13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("video_file")
public class VideoFilePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 文件主键
     */
    private Long fileId;

    /**
     * 是否本地存储
     */
    private Boolean locallyStored;

    /**
     * 存储场景名称
     */
    private String storeSceneCode;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 本地存储状态
     */
    private String locallyStoredStatus;

    /**
     * 重试次数
     */
    private Integer retryTimes;

    /**
     * 下次重试时间
     */
    private Date retryNextTime;
}
