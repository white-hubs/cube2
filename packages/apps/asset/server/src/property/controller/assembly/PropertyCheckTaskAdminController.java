package com.guhecloud.cube.srv.gen.property.management.server.property.controller.assembly;

import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.property.management.client.property.param.assembly.PropertyCheckTaskAdminParam;
import com.guhecloud.cube.property.management.client.property.vo.assembly.PropertyCheckTaskAdminPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.assembly.PropertyCheckTaskAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资产管理员配置
 *
 * @author : lijie
 * @date : 2023-3-20
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyCheckTaskAdminController {
    private final PropertyCheckTaskAdminService propertyCheckTaskAdminService;

    /**
     * 查询
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/property/check/task/admin/getPropertyCheckTaskAdminPage")
    public List<PropertyCheckTaskAdminPageVO> getPropertyCheckTaskAdminPage(@Valid OrganizationQueryParam param) {
        return propertyCheckTaskAdminService.getPropertyCheckTaskAdminPage(param);
    }

    /**
     * 新增数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/property/check/task/admin/insert")
    public Boolean insert(@RequestBody PropertyCheckTaskAdminParam param) {
        return propertyCheckTaskAdminService.insert(param);
    }
}
