package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 检测公司。
     */
    private String detectionCompany;

    /**
     * 检测地址。
     */
    private String detectionAddress;

    /**
     * 检测电话。
     */
    private String detectionPhone;

    /**
     * 检测声明。
     */
    private String detectionStatement;

    /**
     * 检测的印章文件id。
     */
    private Long detectionStampFileId;

    /**
     * 校准期到期时间。
     */
    private LocalDateTime calibrationExpireTime;

    /**
     * 检测倒计时
     */
    private Integer calibrationExpireDay;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 受检单位名称。
     */
    private String tenantName;

    /**
     * 受检租户id。
     */
    private Integer tenantId;

    /**
     * 印章文件对象
     */
    private FileVO fileVO;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
