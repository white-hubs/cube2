package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 角色关联访问端
 *
 * @author lanchun
 * @date 2021/10/20 11:41
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoleTerminal implements Entity<Long> {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 访问端id
     */
    private Long terminalId;

}
