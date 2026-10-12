package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class CameraMultipleVideoQueryParam extends Paging {

    /**
     * 设备名称或者编号搜索
     */
    private String keyword;

    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 所属区域id
     */
    private String areaId;

    private String area;

    /**
     * 设备状态：true:在线,false:离线
     */
    private Boolean status;

    /**
     * 标签集合
     */
    private List<String> tagCodes;
}
