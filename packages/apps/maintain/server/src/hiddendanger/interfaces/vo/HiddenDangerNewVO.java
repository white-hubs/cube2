package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 隐患排查2.0
 * @date 2023/11/8 20:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HiddenDangerNewVO extends ProcessInstanceDTO {
    /**
     * 隐患单号
     */
    private String code;

    /**
     * 隐患名称
     */
    private String name;

    /**
     * 隐患对象名称
     */
    private String hiddenObjName;

    /**
     * 隐患类型
     */
    private String hiddenType;

    /**
     * 隐患来源
     */
    private String source;

    /**
     * 隐患程度
     */
    private String urgencyLevel;

    /**
     * 处理结果
     */
    private String result;
}
