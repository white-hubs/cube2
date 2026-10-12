package com.guhecloud.cube.biz.ouaa.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 应用访问历史记录
 *
 * @author lanchun
 * @date 2022/8/9 10:21
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ApplicationVisitHistory extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 终端编号
     */
    private String terminal;

    /**
     * 访问时间
     */
    private LocalDateTime visitTime;
}
