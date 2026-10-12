package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeStyleCommandService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeStyleCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.enumeration.QrcodeEnum;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.EntityTreeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeStyleVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.QrcodeStyleQueryService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query.QrCodeBatchDeleteParam;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query.QrcodeQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * @author bq
 * @date 2022/7/20
 * @description 二维码样式
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeStyleController {

    private final QrcodeStyleCommandService commandService;
    private final QrcodeStyleQueryService qrcodeStyleQueryService;

    /**
     * 实体类型列表
     *
     * @return
     */
    @GetMapping("/qrcode/style/entity/type")
    public List<EntityTreeVO> getEntityTypeList() {
        return QrcodeEnum.getEntityTree();
    }

    /**
     * 详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/qrcode/style/{id}")
    public QrcodeStyleVO getById(@PathVariable("id") Long id) {
        QrcodeStylePO byId = qrcodeStyleQueryService.getById(id);
        return ObjectUtils.copying(byId, QrcodeStyleVO.class);
    }

    /**
     * 列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/qrcode/style/list")
    public List<QrcodeStyleVO> getList(QrcodeQueryParam param) {
        return qrcodeStyleQueryService.getList(param);
    }

    /**
     * 分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/qrcode/style/page")
    public Pagination<QrcodeStyleVO> getPage(QrcodeQueryParam param) {
        return qrcodeStyleQueryService.getPage(param);
    }

    /**
     * 保存样式
     *
     * @param command
     */
    @PostMapping("/qrcode/style")
    public void save(@Valid @RequestBody QrcodeStyleCommand command) {
        commandService.save(command);
    }

    /**
     * 修改样式
     *
     * @param command
     */
    @PutMapping("/qrcode/style/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody QrcodeStyleCommand command) {
        commandService.update(id, command);
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping("/qrcode/style/{id}")
    public void remove(@PathVariable("id") Long id) {
        commandService.remove(id);
    }

    /**
     * 批量删除
     *
     * @param param
     */
    @DeleteMapping("/qrcode/style/batch")
    public void removeBatch(@Valid @RequestBody QrCodeBatchDeleteParam param) {
        commandService.removeBatch(param.getIds());
    }

}
