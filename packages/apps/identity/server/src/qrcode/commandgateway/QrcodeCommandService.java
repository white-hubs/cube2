package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeBatchGenerateCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.PropertyQrCodeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeApplicationVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeApplicationPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodePO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author bq
 * @date 2022/7/20
 * @description
 */
public interface QrcodeCommandService extends IService<QrcodePO> {

    /**
     * 生成二维码唯一code
     *
     * @return 二维码code
     */
    String generateCode(TenantDTO tenantDTO);

    /**
     * 保存二维码
     *
     * @param command 修改命令
     */
    void save(QrcodeCommand command);

    /**
     * 修改二维码
     *
     * @param id      二维码id
     * @param command 修改命令
     */
    void update(Long id, QrcodeCommand command);

    /**
     * 根据ID删除
     *
     * @param id 二维码id
     */
    void remove(Long id);

    /**
     * 批量删除
     *
     * @param ids 二维码id
     */
    void removeBatch(String ids);

    /**
     * 二维码样式修改之后，同步修改所有引用了该样式的二维码，这样就能保证每个二维码应用的都是新样式
     *
     * @param styleId 样式ID
     * @param json    json配置
     */
    void updateQrcodeByStyle(Long styleId, String json);

    /**
     * @Author wql
     * @Description 二维码批量生成
     * @Date 17:11 2023/11/20
     **/
    List<PropertyQrCodeVO> batchGenerateQRCodeImage(QrcodeBatchGenerateCommand command);

    /**
     * 批量生成并下载二维码图片压缩包
     */
    void batchDownloadQRCodeImage(QrcodeBatchGenerateCommand command, HttpServletResponse response) throws IOException;

    String oldQRCodeCompatible(String type, Long objId);

    /**
     * 按用户权限过滤应用列表
     *
     * @param qrcodeApplications 应用列表
     *
     * @return 应用列表
     */
    List<QrcodeApplicationVO> permissionAppFilter(List<QrcodeApplicationPO> qrcodeApplications);
}
