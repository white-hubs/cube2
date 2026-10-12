package com.guhecloud.cube.srv.gen.business.info.sync.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 一卡通开通表
 *
 * @TableName user_one_card
 */
@Data
@TableName("user_one_card")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOneCard implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 物理卡号
     */
    private String serialNumber;

    /**
     * 状态（OPEN开卡、CANCEL注销）
     */
    private String status;

    /**
     * 有效期
     */
    private LocalDateTime validityDate;

    /**
     * 是否已删除
     */
    private Long deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 租户id
     */
    private Integer tenant;

    private static final long serialVersionUID = -29415411271324496L;
}