package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.dto.TerminalDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.client.param.RenewWorkStatusBatchParam;
import com.guhecloud.cube.biz.ouaa.client.param.RenewWorkStatusParam;
import com.guhecloud.cube.biz.ouaa.client.param.TerminalQueryParam;
import com.guhecloud.cube.biz.ouaa.client.param.WorkStatusQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 用户接口
 *
 * @author Woody
 * @date 2021/7/9
 */
@FeignClient(name = "ouaa")
public interface TerminalClient {

    @GetMapping("/context-terminals/{code}")
    TerminalDTO getTerminalByCode(@PathVariable("code") String code);

    /**
     * 获取人员工作状态
     *
     * @param tenant  租户
     * @param userIds 人员id集合
     *
     * @return 租户人员工作状态
     */
    @GetMapping("/tenant/user/work/status")
    List<UserTenantDTO> getTenantUserWorkStatus(@RequestParam("tenant") Integer tenant, @RequestParam("userIds") Set<Long> userIds);

    /**
     * 获取人员工作状态
     *
     * @param param 条件
     *
     * @return 租户人员工作状态
     */
    @PostMapping("/tenant/user/work/status/post")
    List<UserTenantDTO> getTenantUserWorkStatusPost(@RequestBody WorkStatusQueryParam param);

    /**
     * 更新租户人员工作状态
     *
     * @param param 更新对象
     */
    @PostMapping("/tenant/renew/work/status")
    void updateUserTenantWorkStatus(@RequestBody RenewWorkStatusParam param);

    /**
     * 批量更新租户人员工作状态
     *
     * @param param
     */
    @PostMapping("/tenant/renew/work/status/batch")
    void batchUpdateTenantUserWorkStatus(@RequestBody RenewWorkStatusBatchParam param);

    /**
     * 获取当前租户的终端分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/terminal-under-tenant/page")
    Pagination<TerminalDTO> listByPage(@SpringQueryMap TerminalQueryParam param);
}
