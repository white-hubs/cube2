package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 知识文档提交对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBaseCommand {

    private Long id;

    /**
     * 资料名称
     */
    @NotEmpty(message = "资料名称不能为空!")
    private String name;

    /**
     * 资料类型id
     */
    @NotNull(message = "资料类型不能为空!")
    private Long typeId;

    /**
     * 所属部门主键
     */
    private Long deptId;

    /**
     * 所属产品id
     */
    private Long productId;

    /**
     * 文件主键集合
     */
    @NotEmpty(message = "文件不能为空!")
    private List<Long> fileIds;

    /**
     * id集合(用作批量删除)
     */
    private List<Long> idList;
}
