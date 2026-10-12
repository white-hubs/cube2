package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityStyleService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityTypeService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeEntityTypeCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.EntityStyleVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityTypeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query.QrcodeQueryParam;
import com.guhecloud.platform.base.model.Pagination;
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
 * 实体类型
 *
 * @author wql
 * @describe 实体类型
 * @date 2023/10/23 10:48
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeEntityTypeController {
    private final QrcodeEntityTypeService qrcodeEntityTypeService;
    private final QrcodeEntityStyleService qrcodeEntityStyleService;

    /**
     * 新增或更新实体类型
     *
     * @Author wql
     * @Description 新增或更新实体类型
     * @Date 11:08 2023/10/23
     **/
    @PostMapping("/entity/type/save")
    public void saveType(@Valid @RequestBody QrcodeEntityTypeCommand typeCommand) {
        qrcodeEntityTypeService.saveType(typeCommand);
    }

    /**
     * 删除实体类型
     *
     * @Author wql
     * @Description 删除实体类型
     * @Date 11:08 2023/10/23
     **/
    @DeleteMapping("/entity/type/delete/{id}")
    public void deleteType(@PathVariable("id") Long id) {
        qrcodeEntityTypeService.removeById(id);
    }

    /**
     * 实体类型列表
     *
     * @Author wql
     * @Description 实体类型列表
     * @Date 11:10 2023/10/23
     **/
    @GetMapping("/entity/type/list")
    public List<QrcodeEntityTypeVO> typeList(String keyword) {
        return qrcodeEntityTypeService.typeList(keyword);
    }

    /**
     * 实体类型绑定的二维码样式列表
     *
     * @param param 查询条件
     *
     * @return 二维码样式列表
     */
    @GetMapping("/entity/style/page")
    public Pagination<EntityStyleVO> entityTypeStyle(QrcodeQueryParam param) {
        return qrcodeEntityTypeService.entityTypeStyle(param);
    }

    /**
     * 绑定实体类型二维码样式
     *
     * @param entityType 实体类型
     * @param styleId    二维码样式
     */
    @PutMapping("/entity/{entityType}/bind/{styleId}")
    public void bindEntityTypeStyle(@PathVariable("entityType") Long entityType, @PathVariable("styleId") Long styleId) {
        qrcodeEntityStyleService.bindEntityTypeStyle(entityType, styleId);
    }

    /**
     * 解绑实体类型二维码样式
     *
     * @param entityType 实体类型
     * @param styleId    二维码样式
     */
    @DeleteMapping("/entity/{entityType}/unbind/{styleId}")
    public void unbindEntityTypeStyle(@PathVariable("entityType") Long entityType, @PathVariable("styleId") Long styleId) {
        qrcodeEntityStyleService.unbindEntityTypeStyle(entityType, styleId);
    }

}
