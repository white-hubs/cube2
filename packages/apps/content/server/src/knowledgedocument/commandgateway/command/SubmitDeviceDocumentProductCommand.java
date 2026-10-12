package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

/**
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Data
public class SubmitDeviceDocumentProductCommand {

    /**
     * 设备产品主键
     */
    @NotNull(message = "设备产品主键不能小于1")
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    @NotNull(message = "设备产品名称不能为空")
    @Length(max = 50, message = "设备产品名称")
    private String deviceProdName;
}
