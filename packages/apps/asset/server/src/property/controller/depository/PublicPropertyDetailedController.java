package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资产明细
 *
 * @author : lijie
 * @date : 2023-3-8
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicPropertyDetailedController {

    private final PropertyDetailedController propertyDetailedController;

    /**
     * 公共通过ID查询详情
     *
     * @param id
     *
     * @return 资产详情
     */
    @GetMapping("/public/property/detailed/{id}")
    public PropertyDetailedVO queryById(@PathVariable Long id) {
        return propertyDetailedController.queryById(id);
    }

}
