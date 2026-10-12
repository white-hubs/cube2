package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 设备相关通知（目前只有到期通知）,顶层DTO
 * </p>
 *
 * @author bq
 * @since 2022-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceNoticeSupDTO implements Serializable {

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
    private List<DeviceNoticeDTO> noticeDTOS;

}
