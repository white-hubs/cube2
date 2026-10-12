package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangliang
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_response")
public class WholesalerResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 档位id
     */
    private String stallId;

    /**
     * 问卷ID
     */
    private String questionnaireId;

    /**
     * 模板ID
     */
    private String tempId;

    /**
     * 问题iD
     */
    private String questionId;

    /**
     * 回答
     */
    private String answer;


}
