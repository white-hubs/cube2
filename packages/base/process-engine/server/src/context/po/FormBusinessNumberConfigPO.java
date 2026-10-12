package com.guhecloud.cube.base.flow.context.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单业务编号配置
 */
@Data
@TableName("form_business_number_config")
public class FormBusinessNumberConfigPO {

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 编号前缀
     */
    private String formNoPrefix;

}