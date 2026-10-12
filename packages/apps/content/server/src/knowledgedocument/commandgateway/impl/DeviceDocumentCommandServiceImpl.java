package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration.ContentFileSceneCode;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.DeviceDocumentCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.DeviceDocumentRefProductService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.SubmitDeviceDocumentCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.DeviceDocumentMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocument;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocumentRefProduct;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2022/5/23
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceDocumentCommandServiceImpl extends ServiceImpl<DeviceDocumentMapper, DeviceDocument> implements DeviceDocumentCommandService {

    private final FileRefClient fileRefClient;
    private final UserClient userClient;
    private final DeviceDocumentRefProductService deviceDocumentRefProductService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull SubmitDeviceDocumentCommand command) {
        // 构建信息
        Long businessKeys = this.saveDeviceDocument(command);

        // 新增文件
        this.saveFile(command, businessKeys);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
        List<DeviceDocumentRefProduct> documentRefProducts = this.deviceDocumentRefProductService.list(Wrappers
                                                                                                               .<DeviceDocumentRefProduct>lambdaQuery()
                                                                                                               .eq(DeviceDocumentRefProduct::getDeviceDocumentId, id));
        if (!CollectionUtils.isEmpty(documentRefProducts)) {
            DatabaseContextHolder.softDelete(DeviceDocumentRefProduct.class, documentRefProducts);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemove(List<Long> ids) {
        ids.forEach(this::remove);
    }

    private void saveFile(SubmitDeviceDocumentCommand command, Long businessKey) {
        if (Objects.nonNull(command.getFileId())) {
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setBusinessKey(businessKey);
            fileRefCreateParam.setFileId(command.getFileId());
            fileRefCreateParam.setSceneCode(ContentFileSceneCode.CONTENT_DEVICE_DOCUMENT.getCode());
            this.fileRefClient.refInduce(fileRefCreateParam);
        }
    }

    private Long saveDeviceDocument(SubmitDeviceDocumentCommand command) {
        // 获取用户信息
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDTO user = this.userClient.getUser(userId);
        long id = IdWorker.getId();
        DeviceDocument deviceDocument = ObjectUtils.copying(command, DeviceDocument.class);
        deviceDocument.setId(id);
        if (!CollectionUtils.isEmpty(command.getProducts())) {
            List<DeviceDocumentRefProduct> deviceDocumentRefProducts = command.getProducts()
                    .stream()
                    .map(submitDeviceDocumentProductCommand -> {
                        DeviceDocumentRefProduct deviceDocumentRefProduct = new DeviceDocumentRefProduct();
                        deviceDocumentRefProduct.setDeviceDocumentId(id);
                        deviceDocumentRefProduct.setDeviceProdId(submitDeviceDocumentProductCommand.getDeviceProdId());
                        deviceDocumentRefProduct.setDeviceProdName(submitDeviceDocumentProductCommand.getDeviceProdName());
                        return deviceDocumentRefProduct;
                    }).collect(Collectors.toList());
            this.deviceDocumentRefProductService.saveBatch(deviceDocumentRefProducts);
        }
        deviceDocument.setUserId(userId);
        deviceDocument.setSubmitter(user == null ? "" : user.getName());
        this.save(deviceDocument);
        return id;
    }
}
