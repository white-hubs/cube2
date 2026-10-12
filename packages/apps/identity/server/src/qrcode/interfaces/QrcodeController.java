package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeCommandService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeBatchGenerateCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeEntityApplicationMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeThirdLinkMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.PropertyQrCodeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeApplicationVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityInfoVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeThirdLinkVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeApplicationPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeThirdLinkPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.QrcodeQueryService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.handler.QrcodeStyleInstHandler;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query.QrCodeBatchDeleteParam;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query.QrcodeQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/7/22
 * @description 二维码
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeController {
    private final QrcodeCommandService qrcodeCommandService;
    private final QrcodeQueryService qrcodeQueryService;
    private final QrcodeThirdLinkMapper qrcodeThirdLinkMapper;
    private final QrcodeEntityApplicationMapper qrcodeEntityApplicationMapper;
    private final TenantClient tenantClient;
    private final QrcodeStyleInstHandler qrcodeStyleInstHandler;

    /**
     * 详情
     *
     * @param id 二维码id
     *
     * @return 二维码详情
     */
    @GetMapping("/qrcode/{id}")
    public QrcodeVO getById(@PathVariable("id") Long id) {
        QrcodePO byId = qrcodeQueryService.getById(id);
        Assert.isNotFound(byId, "未找到相关的二维码信息");
        //查询二维码的三方链接
        List<QrcodeThirdLinkPO> thirdLinks = qrcodeThirdLinkMapper.selectList(Wrappers.<QrcodeThirdLinkPO>lambdaQuery()
                                                                                      .eq(QrcodeThirdLinkPO::getQrcodeId, byId.getId()));
        QrcodeVO qrcodeVO = ObjectUtils.copying(byId, QrcodeVO.class);
        if (CollectionUtils.isNotEmpty(thirdLinks)) {
            qrcodeVO.setThirdLinks(ObjectUtils.copying(thirdLinks, QrcodeThirdLinkVO.class));
        }
        qrcodeVO.setConfigJson(qrcodeStyleInstHandler.fillQrcodeBase64(qrcodeVO.getCode(), qrcodeVO.getConfigJson()));
        return qrcodeVO;
    }

    /**
     * 分页列表
     *
     * @param param 查询参数
     *
     * @return 二维码列表
     */
    @GetMapping("/qrcode/page")
    public Pagination<QrcodeVO> getPage(QrcodeQueryParam param) {
        return qrcodeQueryService.getPage(param);
    }

    /**
     * 预览
     *
     * @param command 预览参数
     *
     * @return 二维码
     */
    @PostMapping("/qrcode/preview")
    public String preview(@RequestBody QrcodeCommand command) {
        return qrcodeQueryService.preview(command);
    }

    /**
     * 根据code查询二维码绑定的实体
     *
     * @param code 二维码code
     *
     * @return 二维码实体信息
     */
    @GetMapping("/qrcode/code")
    public QrcodeEntityVO getEntityByCode(@RequestParam(value = "code") String code) {
        QrcodePO byId = qrcodeQueryService.lambdaQuery().eq(QrcodePO::getCode, code).one();
        return ObjectUtils.copying(byId, QrcodeEntityVO.class);
    }

    /**
     * 根据实体ID查询二维码信息
     *
     * @param entityId 实体ID
     *
     * @return 二维码信息
     */
    @GetMapping("/qrcode/entity")
    public QrcodeVO getQrcodeByEntityId(@RequestParam(value = "entityId") String entityId) {
        QrcodePO qrcodePO = qrcodeQueryService.lambdaQuery().eq(QrcodePO::getEntityId, entityId).one();
        if (Objects.nonNull(qrcodePO)) {
            qrcodePO.setConfigJson(qrcodeStyleInstHandler.fillQrcodeBase64(qrcodePO.getCode(), qrcodePO.getConfigJson()));
        }
        return ObjectUtils.copying(qrcodePO, QrcodeVO.class);
    }

    /**
     * 批量查询实体关联的二维码 code。
     *
     * @param entityIds 实体 ID 集合
     *
     * @return 仅包含 entityId 与 code 的二维码信息
     */
    @PostMapping("/qrcode/entity-codes")
    public List<QrcodeVO> getQrcodeCodesByEntityIds(@RequestBody List<Long> entityIds) {
        if (CollectionUtils.isEmpty(entityIds)) {
            return Collections.emptyList();
        }
        List<Long> distinctEntityIds = entityIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(distinctEntityIds)) {
            return Collections.emptyList();
        }
        List<QrcodePO> qrcodes = qrcodeQueryService.lambdaQuery()
                .select(QrcodePO::getEntityId, QrcodePO::getCode)
                .in(QrcodePO::getEntityId, distinctEntityIds)
                .list();
        return ObjectUtils.copying(qrcodes, QrcodeVO.class);
    }

    /**
     * 生成code
     */
    @GetMapping("/qrcode/generate/code")
    public String generateCode() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        TenantDTO tenantDTO = tenantClient.getByCode(tenant);
        Assert.isNotFound(tenantDTO, "未找到租户信息");
        return qrcodeCommandService.generateCode(tenantDTO);
    }

    /**
     * @Author wql
     * @Description 批量生成二维码
     * @Date 16:02 2023/11/20
     **/
    @PostMapping("/qrcode/generate/batch")
    public List<PropertyQrCodeVO> batchGenerateQRCodeImage(@Valid @RequestBody QrcodeBatchGenerateCommand command) {
        return qrcodeCommandService.batchGenerateQRCodeImage(command);
    }

    /**
     * 批量生成并下载二维码图片压缩包
     */
    @PostMapping("/qrcode/generate/batch/download")
    public void batchDownloadQRCodeImage(@RequestBody QrcodeBatchGenerateCommand command,
                                         HttpServletResponse response
                                        ) throws IOException {
        qrcodeCommandService.batchDownloadQRCodeImage(command, response);
    }

    /**
     * 一期二维码扫码兼容
     *
     * @param objId 对象id
     *
     * @return 二维码code
     */
    @GetMapping("/qrcode/oldCode/{objId}")
    public String oldQRCodeCompatible(@PathVariable("objId") Long objId, String type) {
        return qrcodeCommandService.oldQRCodeCompatible(type, objId);
    }

    /**
     * 一期二维码扫码兼容（不鉴权）
     *
     * @param objId 对象id
     *
     * @return 二维码code
     */
    @GetMapping("/public/qrcode/oldCode/{objId}")
    public String oldQRCodeCompatiblePublic(@PathVariable("objId") Long objId, String type) {
        return qrcodeCommandService.oldQRCodeCompatible(type, objId);
    }

    /**
     * 新增
     *
     * @param command 新增信息
     */
    @PostMapping("/qrcode")
    public void save(@Valid @RequestBody QrcodeCommand command) {
        qrcodeCommandService.save(command);
    }

    /**
     * 修改
     *
     * @param command 修改信息
     */
    @PutMapping("/qrcode/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody QrcodeCommand command) {
        qrcodeCommandService.update(id, command);
    }

    /**
     * 删除
     *
     * @param id 二维码id
     */
    @DeleteMapping("/qrcode/{id}")
    public void remove(@PathVariable("id") Long id) {
        qrcodeCommandService.remove(id);
    }

    /**
     * 批量删除
     *
     * @param param 二维码id集合
     */
    @DeleteMapping("/qrcode/batch")
    public void removeBatch(@Valid @RequestBody QrCodeBatchDeleteParam param) {
        qrcodeCommandService.removeBatch(param.getIds());
    }

    /**
     * 扫描二维码，查询集合二维码包含的功能
     *
     * @param code 二维码code
     *
     * @return 二维码包含的应用信息
     */
    @GetMapping("/qrcode/scan/application")
    public List<QrcodeApplicationVO> getFunByQrcode(@RequestParam(value = "code") String code) {
        QrcodePO byId = qrcodeQueryService.lambdaQuery().eq(QrcodePO::getCode, code).one();
        Assert.isNotFound(byId, "未查询到二维码信息，请确认");
        return getApplicationFunction(byId);
    }

    /**
     * 二维码1.1迭代
     *
     * @param code 二维码code
     *
     * @return 二维码包含的实体信息和应用信息
     */
    @GetMapping("/qrcode/scan/entityAndApplication")
    public QrcodeEntityInfoVO getEntityAndFunByQrcode(@RequestParam(value = "code") String code) {
        QrcodePO byId = qrcodeQueryService.lambdaQuery().eq(QrcodePO::getCode, code).one();
        Assert.isNotFound(byId, "未查询到二维码信息，请确认");
        return QrcodeEntityInfoVO.builder()
                .qrcodeApplicationVOS(getApplicationFunction(byId))
                .entityType(byId.getEntityType())
                .entityName(byId.getEntityName())
                .entityId(byId.getEntityId())
                .build();
    }

    /**
     * 根据二维码获取租户
     *
     * @param code
     *
     * @return
     */
    @GetMapping("/public/qrcode/scan/get/tenant")
    public Integer publicGetTenantByQrCode(@RequestParam(value = "code") String code) {
        return qrcodeQueryService.getTenantByQrCode(code);
    }

    @GetMapping("/public/qrcode/scan/entityAndApplication")
    public QrcodeEntityInfoVO publicGetEntityAndFunByQrcode(@RequestParam(value = "code") String code) {
        return getEntityAndFunByQrcode(code);
    }

    private List<QrcodeApplicationVO> getApplicationFunction(QrcodePO byId) {
        //查询二维码涉及的功能
        List<QrcodeApplicationPO> qrcodeApplications = qrcodeEntityApplicationMapper.getEntityTypeApplication(byId.getEntityType());
        //查询二维码第三方链接
        List<QrcodeThirdLinkPO> qrcodeThirdLinkPOS = qrcodeThirdLinkMapper.selectList(Wrappers.<QrcodeThirdLinkPO>lambdaQuery()
                                                                                              .eq(QrcodeThirdLinkPO::getQrcodeId, byId.getId()));
        List<QrcodeApplicationVO> finalApplication = qrcodeCommandService.permissionAppFilter(qrcodeApplications);
        if (CollectionUtils.isNotEmpty(qrcodeThirdLinkPOS)) {
            for (QrcodeThirdLinkPO linkPO : qrcodeThirdLinkPOS) {
                finalApplication.add(new QrcodeApplicationVO("", linkPO.getThirdLinkName(), linkPO.getThirdLinkUrl()));
            }
        }
        return finalApplication;
    }

    /**
     * 查询二维码包含的第三方链接信息,public接口
     *
     * @Author wql
     * @Description 查询二维码包含的第三方链接信息
     * @Date 16:15 2023/10/27
     **/
    @GetMapping("/public/qrcode/scan/thirdLink")
    public List<QrcodeThirdLinkPO> getThirdLink(@RequestParam(value = "code") String code) {
        QrcodePO byId = qrcodeQueryService.lambdaQuery().eq(QrcodePO::getCode, code).one();
        Assert.isNotFound(byId, "未查询到二维码信息，请确认");
        //查询二维码第三方链接
        return qrcodeThirdLinkMapper.selectList(Wrappers.<QrcodeThirdLinkPO>lambdaQuery().eq(QrcodeThirdLinkPO::getQrcodeId, byId.getId()));
    }

    /**
     * 历史二维码数据清洗迁移
     *
     * @param type 对象类型
     *
     * @return 处理结果
     */
    @GetMapping("/public/qrcode/dataTransfer")
    public String oldQrcodeTransfer(@RequestParam("type") String type, @RequestParam("start") Integer start) {
        String result = "";
        try {
            result = qrcodeQueryService.oldQrcodeTransfer(type, start);
        } catch (Exception e) {
            log.error("二维码数据清洗迁移异常:", e);
            result = "二维码数据清洗迁移异常";
        }
        return result;
    }

}
