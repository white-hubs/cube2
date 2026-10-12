package com.guhecloud.cube.srv.gen.identity.server.qrcode.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeStyleMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeValidator {

    private final QrcodeStyleMapper mapper;
    private final QrcodeMapper qrcodeMapper;

    /**
     * 验证二维码样式名称是否相同
     *
     * @param id   二维码样式ID
     * @param name 名称
     */
    public void verifyIsExistByStyleName(Long id, String name) {
        QrcodeStylePO po = mapper.selectOne(Wrappers.<QrcodeStylePO>lambdaQuery()
                                                    .eq(QrcodeStylePO::getName, name)
                                                    .ne(null != id, QrcodeStylePO::getId, id)
                                           );
        Assert.isConflict(Objects.nonNull(po), "存在相同名称!");
    }

    /**
     * 验证二维码名称是否相同
     *
     * @param id   二维码ID
     * @param name 名称
     */
    public void verifyIsExistByQrcodeName(Long id, String name) {
        List<QrcodePO> pos = qrcodeMapper.selectList(Wrappers.<QrcodePO>lambdaQuery()
                                                             .eq(QrcodePO::getName, name)
                                                             .ne(null != id, QrcodePO::getId, id)
                                                    );
        Assert.isConflict(CollectionUtils.isNotEmpty(pos), "存在相同名称!");
    }

    /**
     * 验证二维码code是否相同
     *
     * @param id   二维码ID
     * @param code 编码
     */
    public void verifyIsExistByCode(Long id, String code) {
        List<QrcodePO> pos = qrcodeMapper.selectList(Wrappers.<QrcodePO>lambdaQuery()
                                                             .eq(QrcodePO::getCode, code)
                                                             .ne(null != id, QrcodePO::getId, id)
                                                    );
        Assert.isConflict(CollectionUtils.isNotEmpty(pos), "存在相同二维码Code!");
    }

    /**
     * 验证二维码的实体类型和实体是否都存在
     *
     * @param command
     */
    public void verifyHasEntityAndType(QrcodeCommand command) {
        if (StringUtils.isNotEmpty(command.getEntityType())) {
            Assert.isParameterError(StringUtils.isEmpty(command.getEntityIdStr()), "当选择了实体类型，必须选择一个实体！");
        } else {
            Assert.isParameterError(StringUtils.isNotEmpty(command.getEntityIdStr()), "当选择了实体，必须指定实体类型！");
        }
    }

    /**
     * 验证绑定的实体是否唯一
     *
     * @param entityType
     * @param entityId
     * @param qrcodeId
     */
    public void verifyEntityIsOnly(String entityType, Long entityId, Long qrcodeId) {
        if (StringUtils.isNotEmpty(entityType) && entityId != null) {
            List<QrcodePO> pos = qrcodeMapper.selectList(Wrappers.<QrcodePO>lambdaQuery()
                                                                 .eq(QrcodePO::getEntityType, entityType)
                                                                 .eq(QrcodePO::getEntityId, entityId)
                                                                 .ne(null != qrcodeId, QrcodePO::getId, qrcodeId)
                                                        );
            Assert.isConflict(CollectionUtils.isNotEmpty(pos), "绑定失败，该实体已经绑定了二维码，请勿重复绑定！");
        }
    }

}
