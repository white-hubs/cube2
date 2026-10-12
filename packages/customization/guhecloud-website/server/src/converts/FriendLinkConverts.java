package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.FriendLink;
import com.guhecloud.cube.customized.guhecloudwebsite.param.FriendLinkParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.FriendLinkVO;

public final class FriendLinkConverts {

    private FriendLinkConverts() {
    }

    public static FriendLink toEntity(FriendLinkParam param) {
        FriendLink entity = new FriendLink();
        entity.setLinkName(param.getLinkName());
        entity.setLinkUrl(param.getLinkUrl());
        entity.setStatus(param.getStatus());
        return entity;
    }

    public static FriendLinkVO toVO(FriendLink entity) {
        FriendLinkVO vo = new FriendLinkVO();
        vo.setId(entity.getId());
        vo.setLinkName(entity.getLinkName());
        vo.setLinkUrl(entity.getLinkUrl());
        vo.setStatus(entity.getStatus());
        return vo;
    }
}
