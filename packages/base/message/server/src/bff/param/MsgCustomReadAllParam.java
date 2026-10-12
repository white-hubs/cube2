package com.guhecloud.cube.base.message2.server.bff.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * 用户自定义消息分组一键已读参数
 *
 * @author lanchun
 * @date 2024/7/8 14:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsgCustomReadAllParam {

    /**
     * 应用编号
     */
    @Size(max = 100, message = "应用编号不能大于100个字符")
    private String applicationCode;

    /**
     * 自定义分类名称
     */
    private String customType;

    /**
     * 类别(区分是自定义类型or应用)
     */
    private String category;

    /**
     * 自定义分组条件
     */
    private MsgCustomGroupParam msgCustomGroup;

    /**
     * 过滤掉的分组条件
     */
    private Set<MsgCustomGroupParam> filterMsgCustomGroups;
}

