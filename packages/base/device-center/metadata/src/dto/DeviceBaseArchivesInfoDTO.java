package com.guhecloud.cube.base.device.center.metadata.dto;

import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * @author pengjian
 * @date 2024/1/10 10:24
 * @description 设备基础档案信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceBaseArchivesInfoDTO {
    /**
     * 设备id
     */
    private Long id;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备编码
     */
    private String code;
    /**
     * 设备状态
     */
    private Status status;

    /**
     * 设备状态信息
     */
    @Data
    public static class Status {
        /**
         * 在线状态
         */
        private boolean onlineState;
        /**
         * 最后通信时间
         */
        private LocalDateTime communicationTime;
    }

    /**
     * 部门信息
     * key: 部门id
     * value: 部门名称
     */
    private Pair<Long, String> department;

    /**
     * 区域信息
     * key: 区域id
     * value: 区域名称
     */
    private Pair<Long, String> area;

    /**
     * 设备管理员
     */
    private Set<Long> managerIds;

    /**
     * 三方设备id
     */
    private String thirdPartyId;

    /**
     * 三方配置
     */
    private String thirdpartyCfg;
    /**
     * 设备图片
     */
    private Long picture;

    private Integer tenant;

    //设备id不一样，对象就不一样,复写equals和hashcode方法
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceBaseArchivesInfoDTO that = (DeviceBaseArchivesInfoDTO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
