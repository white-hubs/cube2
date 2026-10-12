package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.guhecloud.cube.customized.guhecloudwebsite.param.FriendLinkParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.FriendLinkQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.FriendLinkVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 友情链接Service
 */
public interface FriendLinkService {

    /**
     * 新增
     */
    void add(FriendLinkParam param);

    /**
     * 编辑
     */
    void update(Long id, FriendLinkParam param);

    /**
     * 详情
     */
    FriendLinkVO detail(Long id);

    /**
     * 启用、禁用
     */
    void toggleStatus(Long id, Integer status);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 查询已启用列表
     */
    List<FriendLinkVO> list(Integer status);

}
