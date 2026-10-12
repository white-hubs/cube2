package com.guhecloud.cube.biz.door.client.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/11 18:28
 * @Description:
 */
@Data
public class DoorDeviceQueryParam extends Paging {
    //关键字
    private String keyword;
    //类型id
    private Long type;
    //区域id
    private String doorArea;
    //id集合
    private List<Long> ids;
    //第三方设备id
    private List<String> outBusinessIdList;
}
