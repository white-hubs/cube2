package com.guhecloud.cube.biz.mdata.server.merchant.param;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 商品认证新增信息
 */
@Data
public class AuthenticationCreateParam {
    /**
     * 经营性质
     */
    @NotNull(message = "经营性质不能为空")
    private MerchantEnum type;
    /**
     * 姓名
     */
    @NotEmpty(message = "姓名不能为空")
    @Size(max = 20, message = "姓名不能超过20")
    private String name;
    /**
     * 身份证号
     */
    @NotEmpty(message = "身份证不能为空")
    @Size(max = 20, message = "身份证不能大于20")
    private String identityCard;
    /**
     * 经营主体类型
     */
    private MerchantEnum subjectType;

    /**
     * 主体名称
     */
    @Size(max = 20, message = "主体名称不能大于20")
    private String subjectName;

    /**
     * 经营证照号
     */
    @Size(max = 100, message = "营业证号不能大于100")
    private String licenseNo;

    /**
     * 身份证正反面文件主键集合
     */
    @NotEmpty(message = "身份证不能为空")
    private List<Long> fileIds;
}
