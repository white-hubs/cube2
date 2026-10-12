package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceListByGenreVO {
    private Long deviceId;
    /**
     * 设备类型id
     */
    private Long id;
    /**
     * 设备类型名称
     */
    private String genreName;
    /**
     * 设备数量
     */
    private Integer count;

    /**
     * 设备列表
     */
    private List<Instance> instanceList;

    public DeviceListByGenreVO(Long id, String genreName, Integer count) {
        this.genreName = genreName;
        this.count = count;
        this.id = id;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Instance {
        private String code;
        private String deviceGenreName;
        private Long parentId;
        private Long deviceId;
        private String deviceInstanceName;
        private String areaId;
        private String areaName;
        private List<Long> areaIdList;

        public List<Long> getAreaIdList() {
            if (CollectionUtils.isEmpty(areaIdList) && StringUtils.isNotBlank(areaId)) {
                areaIdList = Arrays.stream(areaId.split("-")).map(Long::parseLong).collect(Collectors.toList());
            }
            return areaIdList;
        }
    }
}
