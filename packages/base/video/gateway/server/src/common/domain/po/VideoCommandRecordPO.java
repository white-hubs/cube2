package com.guhecloud.cube.base.videogw.server.common.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("video_command_record")
public class VideoCommandRecordPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 命令
     */
    private String command;

    /**
     * 厂家编号
     */
    private String manufacturerCode;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 结果参数
     */
    private String resultParam;

    /**
     * 命令状态
     */
    private String status;
}
