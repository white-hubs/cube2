package com.guhecloud.cube.srv.gen.supplier.client;

import com.guhecloud.cube.srv.gen.supplier.vo.GenSupplierVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("supplierManagement")
public interface GenSupplierClient {
    @GetMapping("/gen-supplier/inner/list")
    List<GenSupplierVo> listGenSuppliers(@RequestParam("keyword") String keyword, @RequestParam("tenant") Integer tenant);
}
