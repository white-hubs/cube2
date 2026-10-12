package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.controller;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.CloudQrCodeProjectCommandService;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateCloudQrCodeProjectCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateCloudQrCodeProjectCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.CloudQrCodeProjectQueryService;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query.CloudQrCodeProjectQuery;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.CloudQrCodeProjectConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo.CloudQrCodeProjectVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 云二维码项目控制器
 */
@Slf4j
@RestController
@RequestMapping("/cloud-qr-code-projects")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeProjectController {

    private final CloudQrCodeProjectCommandService commandService;
    private final CloudQrCodeProjectQueryService queryService;

    /**
     * 创建云二维码项目
     */
    @PostMapping
    public CloudQrCodeProjectVO create(@RequestBody CreateCloudQrCodeProjectCommand command) {
        var entity = commandService.create(command);
        return CloudQrCodeProjectConverters.entityToVO(entity);
    }

    /**
     * 更新云二维码项目
     */
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody UpdateCloudQrCodeProjectCommand command) {
        commandService.update(id, command);
    }

    /**
     * 删除云二维码项目
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }

    /**
     * 查询云二维码项目详情
     */
    @GetMapping("/{id}")
    public CloudQrCodeProjectVO getById(@PathVariable Long id) {
        var entity = queryService.queryById(id);
        return CloudQrCodeProjectConverters.entityToVO(entity);
    }

    /**
     * 分页查询云二维码项目
     */
    @GetMapping("/page")
    public Pagination<CloudQrCodeProjectVO> page(CloudQrCodeProjectQuery query) {
        var page = queryService.queryProjects(query);
        return PagingUtils.convert(page, page.getRecords().stream().map(CloudQrCodeProjectConverters::entityToVO).toList());
    }

    /**
     * 分页查询云二维码项目
     */
    @GetMapping("/list")
    public List<CloudQrCodeProjectVO> list(CloudQrCodeProjectQuery query) {
        var vos = queryService.queryProjectsList(query);
        return vos.stream().map(CloudQrCodeProjectConverters::entityToVO).toList();
    }
}
