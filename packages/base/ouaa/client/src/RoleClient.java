package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.dto.RoleDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author lzy
 * @date 2021/11/25
 */
@FeignClient(name = "ouaa")
public interface RoleClient {

    /**
     * 获取角色信息
     *
     * @return 角色列表
     */
    @PostMapping("/authenticated/post/role/map")
    Map<Long, RoleDTO> postRoleMap();

    /**
     * 获取角色信息（内部调用）
     *
     * @return 角色列表
     */
    @GetMapping("/authenticated/role/list-by-product")
    public List<RoleDTO> listRoles();

    /**
     * 获取角色分页信息（内部调用）
     *
     * @param paging 分页参数
     *
     * @return 角色信息
     */
    @GetMapping("/authenticated/role/list-by-product/page")
    public Pagination<RoleDTO> pageRoles(@SpringQueryMap Paging paging);

    /**
     * 根据ids获取角色信息
     *
     * @param ids
     *
     * @return
     */
    @GetMapping("/authenticated/role/list/by-ids")
    List<RoleDTO> listRolesByIds(@RequestParam(value = "ids") List<Long> ids);

}
