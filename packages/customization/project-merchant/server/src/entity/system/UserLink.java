package com.guhecloud.cube.deliver.project.merchant.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user_link")
public class UserLink {

    private String id;

    private String userId;

    private String wxopenid;

    private String wxunionid;

}
