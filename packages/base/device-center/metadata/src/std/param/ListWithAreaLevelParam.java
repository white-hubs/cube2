package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/2 11:27
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListWithAreaLevelParam {
    private List<Long> deviceIds;
}
