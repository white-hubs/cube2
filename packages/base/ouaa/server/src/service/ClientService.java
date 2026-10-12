package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Client;
import com.guhecloud.cube.biz.ouaa.server.param.ClientModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ClientQueryParam;

/**
 * 客户端业务操作接口
 *
 * @author Woody
 * @date 2021/7/26
 */
public interface ClientService extends IService<Client> {

    Long save(ClientModifyParam param);

    void update(ClientModifyParam param);

    void updateEnable(String code, Boolean enabled);

    Client getTenantClientByCode(Integer tenant, String code);

    Page<Client> getTenantClientPage(Integer tenant, ClientQueryParam query);

    void update(Long id, ClientModifyParam param);

    void remove(Long id);

    void removeTenantClient(Integer tenant, String code);

    Client getById(Long id);

    Client getByCode(String code);

    Page<Client> listByPage(ClientQueryParam param);
}
