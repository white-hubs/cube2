package com.guhecloud.cube.deliver.project.merchant.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.system.UserLink;

public interface UserLinkService extends IService<UserLink> {

    void saveOrUpdate(String userId, String wxopenid, String wxunionid,Integer tenant);

}
