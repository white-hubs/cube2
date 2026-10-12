package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationPageDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.ClientDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.client.param.ClientQueryParam;
import com.guhecloud.cube.biz.ouaa.client.param.TenantClientModifyParam;
import com.guhecloud.cube.biz.ouaa.client.param.TenantModifyParam;
import com.guhecloud.cube.biz.ouaa.client.param.TenantQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/3
 */
@FeignClient(name = "ouaa")
public interface TenantClient {

    /**
     * 新增租户
     *
     * @param param 新增参数
     *
     * @return 租户信息
     */
    @PostMapping("/authenticated/tenant")
    public TenantDTO addTenant(@Valid @RequestBody TenantModifyParam param);

    @PutMapping("/authenticated/tenant/{code}")
    public TenantDTO updateTenant(@PathVariable("code") Integer code, @Valid @RequestBody TenantModifyParam param);

    @GetMapping("/authenticated/tenant/{code}/code")
    public TenantDTO getByCode(@PathVariable("code") Integer code);

    @GetMapping("/authenticated/tenant/mine")
    public List<TenantDTO> listMyTenants();

    @GetMapping("/authenticated/tenant/list")
    public List<TenantDTO> listTenants(@RequestParam(value = "keyword", required = false) String keyword);

    @GetMapping("/public/tenant/list")
    public List<TenantDTO> publicListTenants(@RequestParam("keyword") String keyword);

    @GetMapping("/authenticated/tenant/page")
    public Pagination<TenantDTO> pageTenants(@SpringQueryMap TenantQueryParam queryParam);

    @GetMapping("/authenticated/tenant/codes")
    public List<TenantDTO> listByCodes(@RequestParam("codes") Collection<Integer> codes);

    @GetMapping("/application/{applicationCode}/tenants")
    public List<TenantDTO> getApplicationTenant(@PathVariable("applicationCode") String applicationCode);

    @GetMapping("/tenant-under-tenant/{tenant}/product-applications")
    public List<ApplicationPageDTO> listTenantProductApplications(@PathVariable("tenant") Integer tenant,
                                                                  @Nullable @RequestParam("productId") Long productId
                                                                 );

    @GetMapping("/authenticated/tenant/category/list")
    List<TenantDTO> listTenantsByCategory(@RequestParam(value = "category") String category);

    /**
     * 获取租户客户端配置列表
     *
     * @return
     */
    @GetMapping("/tenant-client/page")
    Pagination<ClientDTO> getTenantClientPage(@SpringQueryMap ClientQueryParam query);

    /**
     * 新增租户客户端配置
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/inner/tenant-client")
    ClientDTO innerAddTenantClient(@RequestBody @Valid TenantClientModifyParam param);

    /**
     * 移除租户客户端配置
     *
     * @param clientCode 客户端编号
     */
    @DeleteMapping("/tenant-client/{clientCode}")
    void removeClient(@PathVariable("clientCode") String clientCode);
}
