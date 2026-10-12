package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商户相关功能通知市场方（目前合同、认证）
 * </p>
 *
 * @author bq
 * @since 2022-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerNoticeSupDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 通知的详情
     */
    private List<WholesalerNoticeDTO> noticeDTOS;

}
