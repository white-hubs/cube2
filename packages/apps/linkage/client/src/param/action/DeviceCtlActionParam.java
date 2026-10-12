package com.guhecloud.cube.biz.linkage.client.param.action;

import com.alibaba.fastjson2.annotation.JSONType;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.client.param.action.validator.DeviceCtlActionSequenceProvider;
import com.guhecloud.cube.biz.linkage.client.param.action.validator.DeviceCtlConfigSequenceProvider;
import com.guhecloud.cube.biz.linkage.client.validator.provider.WhenEnabledTrueGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;

import java.util.List;

@Data
@JSONType(typeName = "IOT_DEVICE_ACTION")
@NoArgsConstructor
@GroupSequenceProvider(DeviceCtlActionSequenceProvider.class)
public class DeviceCtlActionParam extends ActionParam {

    /**
     * 联动动作配置
     */
    @NotEmpty(message = "联动动作配置不能为空", groups = WhenEnabledTrueGroup.class)
    @Valid
    private List<LinkageIotDeviceProParam> actionConfig;

    public DeviceCtlActionParam(LinkageActionTypeEnum actionType, Boolean enabled, List<LinkageIotDeviceProParam> actionConfig) {
        super(actionType, enabled);
        this.actionConfig = actionConfig;
    }

    @Data
    @GroupSequenceProvider(DeviceCtlConfigSequenceProvider.class)
    public static class LinkageIotDeviceProParam {
        /**
         * 设备产品品类编号
         */
        @NotBlank(message = "设备类型不能为空", groups = WhenEnabledTrueGroup.class)
        private String deviceProdCategoryCode;

        /**
         * 设备产品主键
         */
        @NotNull(message = "设备产品主键不能为空", groups = WhenEnabledTrueGroup.class)
        private Long deviceProdId;

        /**
         * 设备主键
         */
        @NotNull(message = "设备不能为空", groups = WhenEnabledTrueGroup.class)
        private Long id;

        /**
         * 设备编号
         */
        @NotBlank(message = "设备编号不能为空", groups = WhenEnabledTrueGroup.class)
        private String code;

        /**
         * 设备名称
         */
        @NotBlank(message = "设备名称不能为空", groups = WhenEnabledTrueGroup.class)
        private String name;

        /**
         * 属性编号
         */
        @NotBlank(message = "属性编号不能为空", groups = WhenEnabledTrueGroup.class)
        private String propCode;

        /**
         * 属性名称
         */
        @NotBlank(message = "属性名称不能为空", groups = WhenEnabledTrueGroup.class)
        private String propName;

        /**
         * 执行方式
         */
        @NotNull(message = "执行方式不能为空", groups = WhenEnabledTrueGroup.class)
        private ExecuteModeEnum executeMode;

        /**
         * 属性值
         */
        @NotBlank(message = "属性值不能为空", groups = WhenEnabledTrueGroup.class)
        private String propValue;
    }

}
