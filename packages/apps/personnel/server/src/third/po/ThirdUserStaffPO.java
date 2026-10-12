package com.guhecloud.cube.srv.gen.personnel.management.server.third.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author bq
 * @date 2023/03/22
 * @description 第三方用户与员工关联
 */
@Data
@TableName("third_user_staff")
public class ThirdUserStaffPO {

    private Long id;

    private Long staffId;

    private String thirdUserId;

    private String mobile;

    private Integer tenant;

}