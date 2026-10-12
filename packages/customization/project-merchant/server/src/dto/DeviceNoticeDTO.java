package com.guhecloud.cube.deliver.project.merchant.dto;


import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceNotice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 设备相关通知（目前只有到期通知）
 * </p>
 *
 * @author bq
 * @since 2022-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceNoticeDTO extends ConvertionDTO<DeviceNoticeDTO, DeviceNotice> implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeviceNoticeDTO() {
    }

    public DeviceNoticeDTO(DeviceNotice notice) {
        super();
        map(notice);
    }

    public DeviceNoticeDTO(List<DeviceNotice> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 还有多少天到期通知，负数就表示过期多少天
     */
    private Integer days;

    /**
     * 站内信
     */
    private boolean site;

    /**
     * 短信
     */
    private boolean sms;

    /**
     * 微信
     */
    private boolean wx;

    /**
     * 弹窗
     */
//    private boolean pop;

    /**
     * 通知配置
     */
    private List<DeviceNoticeConfigDTO> noticeConfigDTOS;

    private String createBy;

    private LocalDateTime createTime;

}
