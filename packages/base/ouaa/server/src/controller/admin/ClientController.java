package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.entity.Client;
import com.guhecloud.cube.biz.ouaa.server.param.ClientModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ClientQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ClientService;
import com.guhecloud.cube.biz.ouaa.server.vo.ClientPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ClientVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 客户端管理接口
 *
 * @author lanchun
 * @date 2021/10/25 14:09
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
public class ClientController {

    private final ClientService clientService;

    /**
     * 客户端添加
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/client")
    public Long save(@RequestBody @Valid ClientModifyParam param) {
        return clientService.save(param);
    }

    /**
     * 客户端修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/client/{id}")
    public void update(@PathVariable("id") @Min(value = 1, message = "应用主键不能小于1") Long id,
                       @RequestBody @Valid ClientModifyParam param
                      ) {
        clientService.update(id, param);
    }

    /**
     * 客户端删除
     *
     * @param id
     */
    @DeleteMapping("/client/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "应用主键不能小于1") Long id) {
        clientService.remove(id);
    }

    /**
     * 客户端详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/client/{id}")
    public ClientVO getClient(@PathVariable("id") Long id) {
        Client client = clientService.getById(id);
        Assert.isNotFound(client, "不存在客户端信息");
        ClientVO clientVO = ObjectUtils.copying(client, ClientVO.class);
        if (StringUtils.isNotEmpty(client.getTenant())) {
            clientVO.setTenants(Arrays.asList(client.getTenant().split(",")));
        }
        return clientVO;
    }

    /**
     * 客户端列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/clients")
    public Pagination<ClientPageVO> listClients(ClientQueryParam param) {
        Page<Client> page = clientService.listByPage(param);
        List<ClientPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), ClientPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        pageVOS.stream().forEach(clientPageVO -> {
            if (StringUtils.isNotEmpty(clientPageVO.getTenant())) {
                clientPageVO.setTenants(Arrays.asList(clientPageVO.getTenant().split(",")));
            }
        });
        return PagingUtils.convert(page, pageVOS);
    }
}
