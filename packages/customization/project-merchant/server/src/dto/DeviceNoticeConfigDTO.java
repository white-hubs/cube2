package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceNoticeConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 设备通知具体方式配置
 * </p>
 *
 * @author bq
 * @since 2022-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceNoticeConfigDTO extends ConvertionDTO<DeviceNoticeConfigDTO, DeviceNoticeConfig> implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeviceNoticeConfigDTO() {
    }

    public DeviceNoticeConfigDTO(DeviceNoticeConfig notice) {
        super();
        map(notice);
    }

    public DeviceNoticeConfigDTO(List<DeviceNoticeConfig> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 通知ID
     */
    private String noticeId;

    /**
     * 通知对象类型（用户、部门、岗位、部门岗位）
     */
    private String noticeType;

    /**
     * 通知的用户IDS字符串
     */
    private String userIds;

    /**
     * 通知的用户ID数组
     */
    private List<String> userIdList;

    /**
     * 通知的部门IDS字符串
     */
    private String deptIds;

    /**
     * 通知的部门ID数组
     */
    private List<String> deptIdList;

    /**
     * 通知的岗位ID字符串
     */
    private String postIds;

    /**
     * 通知的岗位ID数组
     */
    private List<String> postIdList;

    /**
     * 通知的用户IDS
     */
    private Set<String> noticeUserIds;

}
