package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/24 10:46
 * @Description: 按百分比分发，对象共享
 * 每个人都有独立的任务，任务下对象共享，其他人完成，当前用户对象显示完成，
 * 百分比计算方式：对象记录执行人都为当前用户个数除以/对象总个数 当前用户完成百分比
 * 百分比方式对象是否要回填给主任务
 * 对象只关联当前任务时，才能将任务进行指派
 * <p>
 * 主任务 ==》关联用户
 * 任务 ==〉关联对象
 * <p>
 * 1、 关联用户表中添加对象
 * 2、 添加一张关联表存储，任务和对象的关联关系
 * 3、 数据权限控制
 * 4、 对象固定为一个对象池，（任务去关联、用户去关联）
 * 5、 对象查询通过 当前任务id，找到关联用户表，用户表关联的对象id
 *
 */
@Data
public class DailyTaskAssignParam {

    /**
     * 操作类型
     */
    private String dailyType;

    /**
     * 操作人
     */
    private Long operatorId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务对象
     */
    private List<DailyTaskAssignUserParam> assignUserParams;

    public Long getOperatorId() {
        if (Objects.isNull(operatorId)) {
            operatorId = SecurityContextHolder.getPrincipal().getId();
        }
        return operatorId;
    }
}
