package com.guhecloud.cube.srv.gen.supplier.server.supplier.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class GenSupplierTemplate {

    /**
     * 商家名称
     */

    private String name;
    /**
     * 商家名称
     */

    private String code;
    /**
     * 管理员
     */
    private String adminPhone;

    /**
     * 联系人
     */

    private String liaison;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     * 详细地址
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String address;
    /**
     * 批发商档主
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String shiftMaster;
    /**
     * 档主电话
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String shiftMasterPhone;
    /**
     * 档主身份证
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String shiftMasterCard;
    /**
     * 其他联系方人
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String otherContacts;

}
