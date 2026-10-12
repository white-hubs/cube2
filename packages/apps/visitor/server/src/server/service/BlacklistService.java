package com.guhecloud.cube.srv.gen.visitor.management.server.service;

import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.BlacklistParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.KeyWordPageParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.BlacklistResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 访问配置(时段、权限、黑名单等
 */
public interface BlacklistService {

    /**
     * 黑名单分页
     *
     * @param param 入参
     *
     * @return 黑名单列表
     */
    Pagination<BlacklistResult> blacklistPage(KeyWordPageParam param);

    BlacklistResult blackListDetail(Long id);

    void addBlacklist(BlacklistParam param);

    void removeBlacklist(Long id);
}
