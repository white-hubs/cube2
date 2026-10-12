package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * @author lzy
 * @date 2022/5/30
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceDocumentCollectionQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 品类编号
     */
    @Size(max = 300, message = "品类编号不能大于300")
    private String deviceProdCategoryCodeConcat;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

}
