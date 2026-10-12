package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierUserDTO;
import com.guhecloud.cube.biz.mdata.client.dto.UserDTO;
import com.guhecloud.cube.biz.mdata.client.param.SupplierInsertParam;
import com.guhecloud.cube.biz.mdata.client.param.SupplierQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/4
 */
@FeignClient(name = "mdata")
public interface SupplierClient {

    /**
     * 新增批发商并认证
     *
     * @param param
     */
    @PostMapping("/supplier/inner")
    public Long addSupplierAndAuthentication(@Valid @RequestBody SupplierInsertParam param);

    @PutMapping("/supplier/inner/{id}")
    public void updateSupplier(@PathVariable("id") Long id, @RequestBody SupplierInsertParam param);

    @GetMapping("/supplier/inner/{id}")
    public SupplierDTO getSupplierById(@PathVariable("id") Long id);

    @GetMapping("/supplier/inner/{tenant}/supplier-tenant")
    public SupplierDTO getBySupplierTenant(@PathVariable("tenant") Integer tenant);

    @GetMapping("/supplier/inner/list")
    public List<SupplierDTO> listSuppliers(@RequestParam("keyword") String keyword, @RequestParam("tenant") Integer tenant);

    @GetMapping("/supplier/inner/ids")
    public List<SupplierDTO> listByIds(@RequestParam("ids") Collection<? extends Serializable> ids);

    @GetMapping("/supplier/inner/user-ids")
    public List<SupplierDTO> listByUserIds(@RequestParam("userIds") Collection<? extends Serializable> userIds,
                                           @RequestParam("tenant") Integer tenant
                                          );

    @GetMapping("/supplier/inner/page")
    public Pagination<SupplierDTO> pageSupplier(@SpringQueryMap SupplierQueryParam queryParam);

    @GetMapping("/supplier/inner/authenticated/page")
    public Pagination<SupplierDTO> pageInnerAuthenticatedSupplier(@SpringQueryMap SupplierQueryParam queryParam);

    @GetMapping("/supplier/inner/authenticated/list")
    public List<SupplierDTO> listInnerAuthenticateSupplier(@SpringQueryMap SupplierQueryParam queryParam);

    @GetMapping("/supplier-user/inner/info")
    public SupplierUserDTO getBySupplierIdAndPhone(@RequestParam("supplierId") Long supplierId,
                                                   @RequestParam("phone") String phone,
                                                   @RequestParam("supplierCategory") String supplierCategory
                                                  );

    /**
     * 通过商户ID和电话号码，判断用户是否具有商户的钱包支付授权
     *
     * @param supplierId
     * @param phone
     *
     * @return
     */
    @GetMapping("/supplier-user/inner/auth-wallet-pay")
    public Boolean checkUserWithWalletPay(@RequestParam("supplierId") Long supplierId, @RequestParam("phone") String phone);

    @GetMapping("/supplier-user/{userId}/list")
    public List<SupplierDTO> getBySupplierUser(@RequestParam("userId") Long userId);

    @GetMapping("/supplier/inner/customers")
    public Pagination<UserDTO> getCustomers(@SpringQueryMap SupplierQueryParam queryParam);

    @GetMapping("/supplier/inner/mid-type")
    public List<SupplierDTO> getByMidType(@NotNull(message = "商户标识类型不能为空") @RequestParam("midType") String midType,
                                          @NotNull(message = "商户标识值不能为空") @RequestParam("midValue") String midValue
                                         );
}
