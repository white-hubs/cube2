package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/23
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Data
public class SubmitDeviceDocumentCommand {

    /**
     * 文档名称
     */
    private String name;

    /**
     * 设备产品品类编号
     */
    @Length(max = 50, message = "设备产品品类名称不能大于20")
    private String deviceProdCategoryCode;

    /**
     * 设备产品品类序列号
     */
    @Length(max = 50, message = "设备产品品类名称不能大于200")
    private String deviceProdCategoryCodeConcat;

    /**
     * 设备产品品类名称
     */
    @Length(max = 50, message = "设备产品品类名称不能大于50")
    private String deviceProdCategoryName;

    /**
     * 关联的产品列表
     */
    @Valid
    @NotNull(message = "关联产品不能为空")
    @Size(min = 1, message = "关联产品不能小于1")
    private List<SubmitDeviceDocumentProductCommand> products;

    /**
     * 文件主键
     */
    private Long fileId;
}
