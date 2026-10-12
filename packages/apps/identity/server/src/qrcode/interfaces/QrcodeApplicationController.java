package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeApplicationService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeApplicationCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeApplicationPO;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 应用管理
 *
 * @author wql
 * @describe 应用管理
 * @date 2023/10/23 11:39
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeApplicationController {
    private final QrcodeApplicationService qrcodeApplicationService;

    /**
     * 新增或更新应用
     *
     * @Author wql
     * @Description 新增或更新应用
     * @Date 11:08 2023/10/23
     **/
    @PostMapping("/application/save")
    public void saveApplication(@Valid @RequestBody QrcodeApplicationCommand command) {
        qrcodeApplicationService.saveApplication(command);
    }

    /**
     * 删除应用
     *
     * @Author wql
     * @Description 删除应用
     * @Date 11:08 2023/10/23
     **/
    @DeleteMapping("/application/delete/{id}")
    public void deleteApplication(@PathVariable("id") Long id) {
        qrcodeApplicationService.deleteApplication(id);
    }

    /**
     * 应用列表
     *
     * @Author wql
     * @Description 应用列表
     * @Date 11:10 2023/10/23
     **/
    @GetMapping("/application/list")
    public List<QrcodeApplicationPO> typeList(String keyword) {
        return qrcodeApplicationService.lambdaQuery()
                .like(StringUtils.isNotBlank(keyword), QrcodeApplicationPO::getApplicationName, keyword)
                .list();
    }
}
