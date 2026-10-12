package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo;

import lombok.Data;

import java.util.Objects;

/**
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Data
public class DeviceDocumentRefProductVO {

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceDocumentRefProductVO that = (DeviceDocumentRefProductVO) o;
        return deviceProdId.equals(that.deviceProdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceProdId);
    }
}
