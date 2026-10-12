package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultOptionCommandModel;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 巡检结果
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Data
public class InspectResultSimpleVO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 巡检结果名称
     */
    private String name;

    /**
     * 1 启用 0禁用
     */
    private Boolean enable;

    private String itemNames;

    /**
     * 巡检结果选项
     */
    private List<InspectResultOptionCommandModel> options;

}
