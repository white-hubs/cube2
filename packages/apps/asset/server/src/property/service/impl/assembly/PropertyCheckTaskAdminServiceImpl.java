package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.assembly;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.property.management.client.property.param.assembly.PropertyCheckTaskAdminParam;
import com.guhecloud.cube.property.management.client.property.vo.assembly.PropertyCheckTaskAdminPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.assembly.PropertyCheckTaskAdminMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.assembly.PropertyCheckTaskAdmin;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.assembly.PropertyCheckTaskAdminService;
import com.guhecloud.platform.base.util.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author : lijie
 * @date : 2023-3-20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyCheckTaskAdminServiceImpl extends ServiceImpl<PropertyCheckTaskAdminMapper, PropertyCheckTaskAdmin> implements PropertyCheckTaskAdminService {
    private final OrganizationClient organizationClient;
    private final UserClientService userClientService;

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    @Override
    public List<PropertyCheckTaskAdminPageVO> getPropertyCheckTaskAdminPage(OrganizationQueryParam param) {
        List<OrganizationDTO> orgList = organizationClient.getOrganizations(param);
        List<PropertyCheckTaskAdminPageVO> taskAdmins = ConvertUtils.toList(PropertyCheckTaskAdminPageVO.class, orgList);
        List<PropertyCheckTaskAdmin> adminList = baseMapper.selectList(new LambdaQueryWrapper<>());
        adminList.forEach(ex -> {
            taskAdmins.forEach(o -> {
                if (ex.getOrgId().equals(o.getId())) {
                    o.setAdminId(ex.getAdminId());
                    o.setAdminName(userClientService.getUser(ex.getAdminId()));
                }
            });
        });
        return taskAdmins;
    }

    /**
     * 新增-
     *
     * @param param 信息
     *
     * @return 结果
     */
    @Override
    public Boolean insert(PropertyCheckTaskAdminParam param) {
        this.baseMapper.delete(new LambdaQueryWrapper<PropertyCheckTaskAdmin>()
                                       .eq(PropertyCheckTaskAdmin::getOrgId, param.getOrgId()));
        param.getUserIds().forEach(ex -> {
            PropertyCheckTaskAdmin taskAdmin = new PropertyCheckTaskAdmin();
            taskAdmin.setAdminId(ex);
            taskAdmin.setOrgId(param.getOrgId());
            this.baseMapper.insert(taskAdmin);
        });
        return true;
    }
}
