package com.guhecloud.cube.biz.mdata.client.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/11/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceQueryParam extends Paging {

    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 设备主键集合
     */
    private Collection<? extends Serializable> ids;

    /**
     * 所属区域主键
     */
    private List<String> areaIds;

    /**
     * 设备类型主键
     */
    private String typeId;

    /**
     * 物联绑定
     */
    private Boolean iotBind;

    /**
     * 模型绑定
     */
    private Boolean bimBind;

    /**
     * 在线状态
     */
    private Boolean online;
    /**
     * tag编码
     */
    private String tagCode;

    /**
     * 区域序列号
     */
    private String area;
}
