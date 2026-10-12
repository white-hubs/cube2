package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityApplicationService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeEntityApplicationCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityApplicationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 实体与应用绑定关系
 *
 * @author wql
 * @describe 实体与应用绑定关系
 * @date 2023/10/23 10:48
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeEntityApplicationController {
    private final QrcodeEntityApplicationService qrcodeEntityApplicationService;

    /**
     * 新增或更新实体应用绑定
     *
     * @Author wql
     * @Description 新增或更新实体应用绑定
     * @Date 11:08 2023/10/23
     **/
    @PostMapping("/entity/application/save")
    public void saveEntityApplication(@Valid @RequestBody QrcodeEntityApplicationCommand command) {
        qrcodeEntityApplicationService.saveType(command);
    }

    /**
     * 批量新增或更新应用绑定
     *
     * @Author wql
     * @Description 批量新增或更新应用绑定
     * @Date 9:04 2023/11/1
     **/
    @PostMapping("/entity/application/batchSave")
    public void saveEntityApplication(@Valid @RequestBody List<QrcodeEntityApplicationCommand> commands) {
        qrcodeEntityApplicationService.batchSave(commands);
    }

    /**
     * 删除实体与应用绑定
     *
     * @Author wql
     * @Description 删除实体与应用绑定
     * @Date 11:08 2023/10/23
     **/
    @DeleteMapping("/entity/application/delete/{id}")
    public void deleteEntityApplication(@PathVariable("id") Long id) {
        qrcodeEntityApplicationService.removeById(id);
    }

    /**
     * 实体与应用绑定列表
     *
     * @Author wql
     * @Description 实体与应用绑定列表
     * @Date 11:10 2023/10/23
     **/
    @GetMapping("/entity/application/list")
    public List<QrcodeEntityApplicationVO> entityApplicationList(@NotNull Long entityTypeId) {
        return qrcodeEntityApplicationService.entityApplicationList(entityTypeId);
    }
}
