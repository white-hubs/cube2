package com.guhecloud.cube.iot.mgt.dcm.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.cube.iot.mgt.dcm.IotProtocolEnum;
import com.guhecloud.platform.dcm.Identifiable;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;


/**
 * 用于Dcm 的产品模型类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ProdModel implements Identifiable {

    /**
     * 产品品牌(字典key)
     */
    private String brand;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品型号
     */
    private String model = "";

    /**
     * 是否自定义品类 0 否， 1 是
     */
    private Integer isCustom = 0;

    /**
     * 产品品类ID
     */
    private String category;

    /**
     * 节点类型: 1直连设备 2网关设备 3网关子设备
     */
    private String nodeType;

    /**
     * 接入方式Id
     */
    private String accessType;

    /**
     * 消息协议ID（非字典表）
     */
    private String protocol = IotProtocolEnum.gh_iot_protocol.name();

    /**
     * 产品描述
     */
    @Builder.Default
    private String remark = "";

    /**
     * 产品扩展字段,更多描述的可能
     */
    @Builder.Default
    private String extendConfig = "";

    /**
     * 产品状态: 1正常 0禁用
     */
    @Builder.Default
    private Integer status = 1;

    /**
     * 产品图标
     */
    @Builder.Default
    private String iconPath = "";

    /**
     * 产品图片
     */
    @Builder.Default
    private String picPath = "";

    /**
     * 产品属性列表
     */
    @Builder.Default
    private List<ProdProperty> properties = Collections.emptyList();

    /**
     * 产品事件列表
     */
    @Builder.Default
    private List<ProdEvent> events = Collections.emptyList();

    /**
     * 产品命令列表
     */
    @Builder.Default
    private List<ProdCommand> commands = Collections.emptyList();

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s:%s", brand, name, model == null ? "" : model);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s", status, StringUtil.isBlank(extendConfig) ? "" :
                        extendConfig.hashCode(),
                properties.hashCode(),
                events.hashCode());
    }
}
