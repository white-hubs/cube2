package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.CloudQrCodeCommandService;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchCreateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchDeleteQrCodeByCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchUpdateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.DeleteQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeEnabledCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.CloudQrCodeQueryService;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query.CloudQrCodeQuery;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.QrCode;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.client.CloudQrCodeClientService;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.QrCodeConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo.CloudQrCodeVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 二维码云化管理控制器
 *
 * @author system
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeController {

    private final CloudQrCodeCommandService cloudQrCodeCommandService;
    private final CloudQrCodeQueryService cloudQrCodeQueryService;
    private final CloudQrCodeClientService cloudQrCodeClientService;

    /**
     * 分页查询二维码列表
     */
    @GetMapping("/cloud-qr-code/page")
    public Pagination<CloudQrCodeVO> queryCloudQrCodes(CloudQrCodeQuery query) {
        return convertPagination(cloudQrCodeQueryService.queryCloudQrCodes(query));
    }

    /**
     * 根据ID查询二维码详情
     */
    @GetMapping("/cloud-qr-code/{id}")
    public CloudQrCodeVO queryCloudQrCodeById(@PathVariable Long id) {
        var qrCode = cloudQrCodeQueryService.queryCloudQrCodeById(id);
        if (Objects.isNull(qrCode)) {
            return null;
        }
        var vo = QrCodeConverters.entityToVO(qrCode);
        // 填充客户端名称
        fillClientName(vo);
        return vo;
    }

    /**
     * 通过code查询二维码详情
     */
    @GetMapping("/cloud-qr-code/code/{code}")
    public CloudQrCodeVO queryCloudQrCodeByCode(@PathVariable String code) {
        var qrCode = cloudQrCodeQueryService.queryCloudQrCodeByCode(code);
        if (Objects.isNull(qrCode)) {
            return null;
        }
        var vo = QrCodeConverters.entityToVO(qrCode);
        // 填充客户端名称
        fillClientName(vo);
        return vo;
    }

    /**
     * 通过codes查询二维码列表
     *
     * @param codes 编号列表
     *
     * @return 列表
     */
    @PostMapping("/cloud-qr-code/query/by-codes")
    public List<CloudQrCodeVO> queryGrantClientIdAndCodes(@RequestBody List<String> codes) {
        var qrCodes = cloudQrCodeQueryService.queryCloudQrCodeByCodes(codes);
        return qrCodes.stream().map(QrCodeConverters::entityToVO).collect(Collectors.toList());
    }

    /**
     * 创建二维码
     */
    @PostMapping("/cloud-qr-code")
    public CloudQrCodeVO createCloudQrCode(@Valid @RequestBody CreateQrCodeCommand command) {
        // 自动设置grantClientCode为当前客户端的client_id
        if (Objects.isNull(command.getGrantClientId())) {
            var clientId = SecurityContextHolder.getPrincipalProperty(Principal::getAccount);
            command.setGrantClientId(clientId);
        }
        var qrCode = cloudQrCodeCommandService.createCloudQrCodeCommand(command);
        return QrCodeConverters.entityToVO(qrCode);
    }

    /**
     * 更新二维码
     */
    @PutMapping("/cloud-qr-code/{id}")
    public CloudQrCodeVO updateCloudQrCode(@PathVariable Long id, @Valid @RequestBody UpdateQrCodeCommand command) {
        command.setId(id);
        var qrCode = cloudQrCodeCommandService.updateCloudQrCodeCommand(command);
        return QrCodeConverters.entityToVO(qrCode);
    }

    /**
     * 更新二维码状态
     */
    @PutMapping("/cloud-qr-code/enabled/{id}/{enabled}")
    public CloudQrCodeVO updateCloudQrCodeEnabled(@PathVariable("id") Long id, @PathVariable("enabled") Boolean enabled) {
        var command = new UpdateQrCodeEnabledCommand();
        command.setId(id);
        command.setEnabled(enabled);
        var qrCode = cloudQrCodeCommandService.updateCloudQrCodeEnabledCommand(command);
        return QrCodeConverters.entityToVO(qrCode);
    }

    /**
     * 通过code更新二维码
     */
    @PutMapping("/cloud-qr-code/code/{code}")
    public CloudQrCodeVO updateCloudQrCodeByCode(@PathVariable String code, @Valid @RequestBody UpdateQrCodeCommand command) {
        var qrCode = cloudQrCodeCommandService.updateCloudQrCodeByCodeCommand(code, command);
        return QrCodeConverters.entityToVO(qrCode);
    }

    /**
     * 删除二维码
     */
    @DeleteMapping("/cloud-qr-code/{id}")
    public void deleteCloudQrCode(@PathVariable Long id) {
        var command = new DeleteQrCodeCommand();
        command.setId(id);
        cloudQrCodeCommandService.deleteCloudQrCodeCommand(command);
    }

    /**
     * 预览二维码
     *
     * @param id
     *
     * @return
     */
    @PostMapping("/cloud-qr-code/preview/{id}")
    public String previewQrCode(@PathVariable Long id) {
        var qrCode = cloudQrCodeQueryService.queryCloudQrCodeById(id);
        Assert.isNotFound(qrCode, "二维码不存在");
        var config = new QrConfig();
        var os = new FastByteArrayOutputStream();
        try {
            var image = QrCodeUtil.generate(qrCode.getQrContent(), config);
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("二维码生成异常:", e);
            throw new RuntimeException(e);
        }
        byte[] bytes = os.toByteArray();
        return "data:image/jpg;base64," + Base64Utils.encode(bytes);
    }

    /**
     * 通过code删除二维码
     */
    @DeleteMapping("/cloud-qr-code/code/{code}")
    public void deleteCloudQrCodeByCode(@PathVariable String code) {
        cloudQrCodeCommandService.deleteCloudQrCodeByCodeCommand(code);
    }

    /**
     * 批量创建二维码
     */
    @PostMapping("/cloud-qr-code/batch")
    public List<CloudQrCodeVO> batchCreateQrCodes(@Valid @RequestBody BatchCreateQrCodeCommand command) {
        var qrCodes = cloudQrCodeCommandService.batchCreateQrCodes(command);
        return qrCodes.stream().map(QrCodeConverters::entityToVO).toList();
    }

    /**
     * 批量更新二维码
     */
    @PutMapping("/cloud-qr-code/batch")
    public List<CloudQrCodeVO> batchUpdateQrCodes(@Valid @RequestBody BatchUpdateQrCodeCommand command) {
        var qrCodes = cloudQrCodeCommandService.batchUpdateQrCodes(command);
        return qrCodes.stream().map(QrCodeConverters::entityToVO).toList();
    }

    /**
     * 批量通过code删除二维码
     */
    @DeleteMapping("/cloud-qr-code/batch")
    public void batchDeleteQrCodesByCode(@Valid @RequestBody BatchDeleteQrCodeByCodeCommand command) {
        cloudQrCodeCommandService.batchDeleteQrCodesByCode(command);
    }

    /**
     * 根据短码获取云二维码详情
     *
     * @param shortCode 短码
     *
     * @return 二维码详情
     */
    @GetMapping("/public/cloud-qr-code/short-code/{shortCode}")
    public CloudQrCodeVO getCloudQrCodeByShortCode(@PathVariable("shortCode") String shortCode) {
        SessionContextHolder.requestTenant(0);
        var qrCode = cloudQrCodeQueryService.getByShortCode(shortCode);
        if (Objects.isNull(qrCode)) {
            return null;
        }
        return QrCodeConverters.entityToVO(qrCode);
    }

    /**
     * 转换分页对象并批量填充客户端名称
     */
    private Pagination<CloudQrCodeVO> convertPagination(Pagination<QrCode> pagination) {
        var voList = pagination.getRecords().stream()
                .map(QrCodeConverters::entityToVO)
                .toList();

        // 批量填充客户端名称
        fillClientNames(voList);

        return PagingUtils.convert(pagination, voList);
    }

    /**
     * 批量填充客户端名称
     */
    private void fillClientNames(List<CloudQrCodeVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }

        // 收集所有需要查询的clientIds
        var clientIds = voList.stream()
                .map(CloudQrCodeVO::getGrantClientId)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();

        if (CollectionUtils.isEmpty(clientIds)) {
            return;
        }

        // 批量查询客户端名称
        var clientNameMap = cloudQrCodeClientService.getClientNameMap(clientIds);

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
    private void fillClientName(CloudQrCodeVO vo) {
        if (vo != null && StringUtils.isNotBlank(vo.getGrantClientId())) {
            var clientName = cloudQrCodeClientService.getClientName(vo.getGrantClientId());
            vo.setGrantClientName(clientName);
        }
    }
}
