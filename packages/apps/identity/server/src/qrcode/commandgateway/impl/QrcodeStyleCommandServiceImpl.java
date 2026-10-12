package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeCommandService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeStyleCommandService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeStyleCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeStyleJsonCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeStyleMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.factory.QrcodeStyleFactory;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.validator.QrcodeValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/7/20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeStyleCommandServiceImpl extends ServiceImpl<QrcodeStyleMapper, QrcodeStylePO> implements QrcodeStyleCommandService {

    private final QrcodeValidator validator;
    private final FileRefClient fileRefClient;
    private final FileClient fileClient;
    private final QrcodeMapper qrcodeMapper;
    private final QrcodeCommandService qrcodeCommandService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@Nonnull QrcodeStyleCommand command) {
        QrcodeStyleJsonCommand jsonCommand = new QrcodeStyleFactory(command.getConfigJson()).getObject();
        // 验证是否存在相同的名称
        validator.verifyIsExistByStyleName(null, jsonCommand.getName());
        QrcodeStylePO qrcodeStylePO = ObjectUtils.copying(jsonCommand, QrcodeStylePO.class);
        // 保存数据
        qrcodeStylePO.setCode(this.generateCode());
        qrcodeStylePO.setConfigJson(command.getConfigJson());
        // 处理背景图和上传的小图
        this.save(qrcodeStylePO);
        this.buildFiles(qrcodeStylePO.getId(), jsonCommand);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, QrcodeStyleCommand command) {
        if (null == id) {
            return;
        }
        QrcodeStylePO stylePO = this.getById(id);
        if (null == stylePO) {
            return;
        }
        QrcodeStyleJsonCommand jsonCommand = new QrcodeStyleFactory(command.getConfigJson()).getObject();
        // 验证是否存在相同的名称
        validator.verifyIsExistByStyleName(id, jsonCommand.getName());
        ObjectUtils.copying(jsonCommand, stylePO);
        stylePO.setConfigJson(command.getConfigJson());
        this.updateById(stylePO);
        // 前端并未调用删除接口，需后端去删除废弃的图
        this.updateFiles(id, jsonCommand);
        // 修改所有引用这个样式的二维码的样式
        qrcodeCommandService.updateQrcodeByStyle(id, command.getConfigJson());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        // 查询是否被二维码关联了
        List<QrcodePO> qrcodePOS = qrcodeMapper.selectList(Wrappers.<QrcodePO>lambdaQuery().eq(QrcodePO::getStyleId, id));
        Assert.isConflict(CollectionUtils.isNotEmpty(qrcodePOS), "该样式已经被二维码关联，请先解除！");
        // 删除背景图和其他图
        this.fileRefClient.removeByBusinessKey(id);
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(String ids) {
        if (ids.contains(",")) {
            for (String id : ids.split(",")) {
                this.remove(Long.valueOf(id));
            }
        } else {
            this.remove(Long.valueOf(ids));
        }
    }

    /**
     * 生成code，规则：年/月/日+三位自增。如：20220724001
     *
     * @return
     */
    private synchronized String generateCode() {
        String yyyyMMdd = DateUtils.format(DateUtils.now(), "yyyyMMdd");
        // 序号
        int num = 1;
        List<QrcodeStylePO> list = this.lambdaQuery().likeRight(QrcodeStylePO::getCode, yyyyMMdd).orderByDesc(QrcodeStylePO::getCreateTime)
                .list();
        if (list.size() > 0) {
            String lastCodeString = list.get(0).getCode().substring(8);
            num = Integer.parseInt(lastCodeString) + 1;
        }
        // 格式化
        String numStr = String.format("%03d", num);
        return yyyyMMdd + numStr;
    }

    /**
     * 标记二维码上的图片
     *
     * @param businessKey 样式ID
     * @param jsonCommand json对象
     */
    private void buildFiles(Long businessKey, QrcodeStyleJsonCommand jsonCommand) {
        List<Long> imageIds = jsonCommand.getImageIds();
        imageIds.forEach(imageId -> {
            this.fileRefClient.save(businessKey, imageId);
        });
    }

    /**
     * 更新二维码上的图片
     * 1、找到所有标之前标记的图片
     * 2、对比当前最新需要标记的
     * 3、标记新图、删除多余的、其余的不变
     *
     * @param id          样式ID
     * @param jsonCommand json对象
     */
    private void updateFiles(Long id, QrcodeStyleJsonCommand jsonCommand) {
        // 找到之前标记的所有图片
        List<Long> oldFileIds = fileClient.getByBusinessKey(id).stream().map(FileVO::getId).collect(Collectors.toList());
        // 找到现在要标记的所有图片
        List<Long> nowFileIds = jsonCommand.getImageIds();
        if (CollectionUtils.isEmpty(nowFileIds)) {
            fileRefClient.removeByBusinessKey(id);
        } else {
            // 找到新的未标记的图片，进行标记
            List<Long> newFileIds = nowFileIds.stream().filter(fileId -> !oldFileIds.contains(fileId)).collect(Collectors.toList());
            newFileIds.forEach(imageId -> {
                this.fileRefClient.save(id, imageId);
            });
            // 找到之前存在而现在不需要的图片，进行删除
            List<Long> abandonedFileIds = oldFileIds.stream().filter(savedFileId -> !nowFileIds.contains(savedFileId))
                    .collect(Collectors.toList());
            abandonedFileIds.forEach(this.fileClient::remove);
        }
    }

}
