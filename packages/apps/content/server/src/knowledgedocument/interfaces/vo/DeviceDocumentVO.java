package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2022/5/30
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Data
public class DeviceDocumentVO {

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
     * 关联产品信息
     */
    private RefProductConcat product;

    /**
     * 文档格式
     */
    private String format;

    /**
     * 文档大小
     */
    @Decimal
    private BigDecimal size;

    /**
     * 提交人用户主键
     */
    private Long userId;

    /**
     * 提交人用户名称
     */
    private String submitter;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    private FileVO fileVO;

    /**
     * 关联的设备产品信息
     */
    @Data
    public static class RefProductConcat {

        /**
         * 拼接后的产品主键集合
         */
        private String productIds;

        /**
         * 拼接后的产品名称集合
         */
        private String productNames;
    }
}
