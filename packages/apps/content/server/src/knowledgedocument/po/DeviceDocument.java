package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzy
 * @date 2022/5/23
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceDocument extends BasicTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     * 设备产品品类序列号
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 设备产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 提交人用户主键
     */
    private Long userId;

    /**
     * 提交人用户名称
     */
    private String submitter;

    /**
     * 是否全部
     */
    private Boolean relation;

}
