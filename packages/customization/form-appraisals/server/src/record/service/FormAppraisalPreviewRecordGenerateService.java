package com.guhecloud.cube.srv.gen.appraisals.server.record.service;

/**
 * 表单考核预览记录生成保障服务。
 */
public interface FormAppraisalPreviewRecordGenerateService {
    /**
     * 生成或刷新所有周期类型的当前预览数据。
     */
    void generateCurrentPreviews();
}
