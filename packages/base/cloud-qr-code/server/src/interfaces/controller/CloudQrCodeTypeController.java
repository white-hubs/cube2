package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.controller;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.CloudQrCodeTypeCommandService;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchCreateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchDeleteQrCodeTypeByCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchUpdateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.DeleteQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.CloudQrTypeQueryService;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query.CloudQrTypeBatchQuery;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query.CloudQrTypeQuery;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.valueobject.QrCodeType;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.client.CloudQrCodeClientService;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.QrCodeTypeConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo.CloudQrCodeTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.Valid;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 二维码类型管理控制器
 *
 * @author system
 */
@Slf4j
@RestController
@RequestMapping("/cloud-qr-code-type")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeTypeController {

    private final CloudQrCodeTypeCommandService cloudQrCodeTypeCommandService;
    private final CloudQrTypeQueryService cloudQrTypeQueryService;
    private final CloudQrCodeClientService cloudQrCodeClientService;

    /**
     * 分页查询二维码类型列表
     */
    @GetMapping("/page")
    public Pagination<CloudQrCodeTypeVO> queryCloudQrTypes(CloudQrTypeQuery query) {
        return convertPagination(cloudQrTypeQueryService.queryCloudQrTypes(query));
    }

    /**
     * 根据ID查询二维码类型详情
     */
    @GetMapping("/{id}")
    public CloudQrCodeTypeVO queryCloudQrTypeById(@PathVariable Long id) {
        var qrCodeType = cloudQrTypeQueryService.queryCloudQrTypeById(id);
        if (Objects.isNull(qrCodeType)) {
            return null;
        }
        var vo = QrCodeTypeConverters.entityToVO(qrCodeType);
        // 填充客户端名称
        fillClientName(vo);
        return vo;
    }

    /**
     * 创建二维码类型
     */
    @PostMapping
    public CloudQrCodeTypeVO createQrCodeType(@Valid @RequestBody CreateQrCodeTypeCommand command) {
        // 自动设置grantClientCode为当前客户端的client_id
        if (StringUtils.isBlank(command.getGrantClientId())) {
            var clientId = SecurityContextHolder.getPrincipalProperty(Principal::getAccount);
            command.setGrantClientId(clientId);
        }
        var qrCodeType = cloudQrCodeTypeCommandService.createQrCodeTypeCommand(command);
        return QrCodeTypeConverters.entityToVO(qrCodeType);
    }

    /**
     * 更新二维码类型
     */
    @PutMapping("/{id}")
    public CloudQrCodeTypeVO updateQrCodeType(@PathVariable Long id, @Valid @RequestBody UpdateQrCodeTypeCommand command) {
        command.setId(id);
        var qrCodeType = cloudQrCodeTypeCommandService.updateQrCodeTypeCommand(command);
        return QrCodeTypeConverters.entityToVO(qrCodeType);
    }

    /**
     * 通过code更新二维码类型
     */
    @PutMapping("/code/{code}")
    public CloudQrCodeTypeVO updateQrCodeTypeByCode(@PathVariable String code, @Valid @RequestBody UpdateQrCodeTypeCommand command) {
        command.setCode(code);
        var qrCodeType = cloudQrCodeTypeCommandService.updateQrCodeTypeByCodeCommand(command);
        return QrCodeTypeConverters.entityToVO(qrCodeType);
    }

    /**
     * 删除二维码类型
     */
    @DeleteMapping("/{id}")
    public void deleteQrCodeType(@PathVariable Long id) {
        var command = new DeleteQrCodeTypeCommand();
        command.setId(id);
        cloudQrCodeTypeCommandService.deleteQrCodeTypeCommand(command);
    }

    /**
     * 批量创建二维码类型
     */
    @PostMapping("/batch")
    public List<CloudQrCodeTypeVO> batchCreateQrCodeTypes(@Valid @RequestBody BatchCreateQrCodeTypeCommand command) {
        // 自动设置grantClientCode
        var clientId = SecurityContextHolder.getPrincipalProperty(Principal::getAccount);
        command.getItems().forEach(item -> {
            if (StringUtils.isBlank(item.getGrantClientId())) {
                item.setGrantClientId(clientId);
            }
        });
        var qrCodeTypes = cloudQrCodeTypeCommandService.batchCreateQrCodeTypes(command);
        return qrCodeTypes.stream().map(QrCodeTypeConverters::entityToVO).toList();
    }

    /**
     * 批量更新二维码类型
     */
    @PutMapping("/batch")
    public List<CloudQrCodeTypeVO> batchUpdateQrCodeTypes(@Valid @RequestBody BatchUpdateQrCodeTypeCommand command) {
        var qrCodeTypes = cloudQrCodeTypeCommandService.batchUpdateQrCodeTypes(command);
        return qrCodeTypes.stream().map(QrCodeTypeConverters::entityToVO).toList();
    }

    /**
     * 通过code删除二维码类型
     */
    @DeleteMapping("/code/{code}")
    public void deleteQrCodeTypeByCode(@PathVariable String code) {
        var command = new BatchDeleteQrCodeTypeByCodeCommand();
        command.setCodes(Collections.singletonList(code));
        cloudQrCodeTypeCommandService.batchDeleteQrCodeTypesByCode(command);
    }

    /**
     * 批量通过code删除二维码类型
     */
    @DeleteMapping("/batch")
    public void batchDeleteQrCodeTypesByCode(@Valid @RequestBody BatchDeleteQrCodeTypeByCodeCommand command) {
        cloudQrCodeTypeCommandService.batchDeleteQrCodeTypesByCode(command);
    }

    /**
     * 查询二维码类型列表
     *
     * @param query
     *
     * @return
     */
    @PostMapping("/batch/query")
    public List<CloudQrCodeTypeVO> batchQueryQrCodeTypes(@Valid @RequestBody CloudQrTypeBatchQuery query) {
        if (CollectionUtils.isEmpty(query.getCodes())) {
            return Collections.emptyList();
        }
        return cloudQrTypeQueryService.batchQueryQrCodeTypes(query).stream()
                .map(QrCodeTypeConverters::entityToVO)
                .toList();
    }

    /**
     * 转换分页对象并批量填充客户端名称
     */
    private Pagination<CloudQrCodeTypeVO> convertPagination(Pagination<QrCodeType> pagination) {
        var voList = pagination.getRecords().stream()
                .map(QrCodeTypeConverters::entityToVO)
                .toList();
        // 批量填充客户端名称
        fillClientNames(voList);
        return PagingUtils.convert(pagination, voList);
    }

    /**
     * 批量填充客户端名称
     */
    private void fillClientNames(List<CloudQrCodeTypeVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        // 收集所有需要查询的clientCode
        var clientCodes = voList.stream()
                .map(CloudQrCodeTypeVO::getGrantClientId)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
        if (CollectionUtils.isEmpty(clientCodes)) {
            return;
        }
        // 批量查询客户端名称
        var clientNameMap = cloudQrCodeClientService.getClientNameMap(clientCodes);
        // 填充到VO中
        voList.forEach(vo -> {
            if (StringUtils.isNotBlank(vo.getGrantClientId())) {
                vo.setGrantClientName(clientNameMap.get(vo.getGrantClientId()));
            }
        });
    }

    /**
     * 填充单个VO的客户端名称
     */
    private void fillClientName(CloudQrCodeTypeVO vo) {
        if (vo != null && StringUtils.isNotBlank(vo.getGrantClientId())) {
            var clientName = cloudQrCodeClientService.getClientName(vo.getGrantClientId());
            vo.setGrantClientName(clientName);
        }
    }
}
