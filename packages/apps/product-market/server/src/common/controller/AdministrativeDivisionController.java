package com.guhecloud.cube.srv.gen.product.market.server.common.controller;

import com.guhecloud.cube.srv.gen.product.market.server.common.service.AdministrativeDivisionService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.AdministrativeDivisionTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 行政区划
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdministrativeDivisionController {
    private final AdministrativeDivisionService administrativeDivisionService;

    /**
     * 功能描述： 行政区划详情
     */
    @GetMapping(value = "/administrative-division/tree")
    public List<AdministrativeDivisionTreeVO> getTree() {
        return administrativeDivisionService.getAdministrativeDivisionTree();
    }

}