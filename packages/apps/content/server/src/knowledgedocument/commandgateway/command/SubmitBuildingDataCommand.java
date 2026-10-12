package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/19
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Data
public class SubmitBuildingDataCommand {

    /**
     * 资料名称
     */
    @NotEmpty(message = "资料名称不能为空!")
    private String name;

    /**
     * 资料类型
     */
    @NotNull(message = "资料类型不能为空!")
    private Long type;

    /**
     * 所属区域主键
     */
    @NotNull(message = "所属区域主键不能为空!")
    private String areaId;

    /**
     * 所属区域名称
     */
    private String areaName;

    /**
     * 所属部门主键
     */
    @NotNull(message = "所属部门不能为空!")
    private Long deptId;

    /**
     * 所属部门名称
     */
    private String deptName;

    /**
     * 文件主键集合
     */
    @NotEmpty(message = "文件不能为空!")
    private List<Long> fileIds;
}
