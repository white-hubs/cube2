package com.guhecloud.cube.base.device.center.metadata.vo;

import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import lombok.Data;

import java.util.List;

/**
 * 设备关联的标签
 *
 * @author lanchun
 * @date 2024/11/20 11:55
 */
@Data
public class DeviceRefTagVO {

    /**
     * 设备主键
     */
    private Long id;

    /**
     * 标签列表
     */
    private List<CustomerTagVO> tags;
}
