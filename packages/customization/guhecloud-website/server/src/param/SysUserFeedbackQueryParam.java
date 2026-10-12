package com.guhecloud.cube.customized.guhecloudwebsite.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * 用户留言分页查询参数
 */
@Data
public class SysUserFeedbackQueryParam extends Paging {

    /**
     * 姓名（模糊查询）
     */
    private String userName;

    /**
     * 联系方式
     */
    private String contactWay;

    /**
     * 企业名称（模糊查询）
     */
    private String companyName;

}
