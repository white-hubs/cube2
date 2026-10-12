package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.dto.CompanyDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户管理公共接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicMerchantCompanyController {

    private final MerchantCompanyController merchantCompanyController;

    @GetMapping("/public/companies/stallId")
    public CompanyDTO publicGetByStallId(@RequestParam("stallId") String stallId) {
        return merchantCompanyController.getByStallId(stallId);
    }
}
