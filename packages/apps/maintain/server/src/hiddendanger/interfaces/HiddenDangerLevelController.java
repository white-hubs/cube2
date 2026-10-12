package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerLevelCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command.HiddenDangerLevelCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerLevelMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerLevelVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerLevel;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 隐患程度接口
 *
 * @author lzy
 * @date 2022/4/26
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerLevelController {

    private final HiddenDangerLevelCommandService hiddenDangerLevelCommandService;

    private final HiddenDangerLevelMapper hiddenDangerLevelMapper;

    /**
     * 隐患程度新增
     *
     * @param command 新增参数
     */
    @PostMapping("/hidden-danger-level")
    public void save(@Valid @RequestBody HiddenDangerLevelCommand command) {
        this.hiddenDangerLevelCommandService.save(command);
    }

    /**
     * 隐患程度编辑
     *
     * @param id      隐患程度主键
     * @param command 编辑参数
     */
    @PutMapping("/hidden-danger-level/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody HiddenDangerLevelCommand command) {
        this.hiddenDangerLevelCommandService.update(id, command);
    }

    /**
     * 隐患程度删除
     *
     * @param id 隐患程度主键
     */
    @DeleteMapping("/hidden-danger-level/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.hiddenDangerLevelCommandService.remove(id);
    }

    /**
     * 隐患程度列表
     *
     * @return 隐患程度信息
     */
    @GetMapping("/hidden-danger-level/list")
    public List<HiddenDangerLevelVO> listHiddenDangerLevel() {
        // 查询
        List<HiddenDangerLevel> hiddenDangerLevels = this.hiddenDangerLevelMapper.selectList(null);
        return ObjectUtils.copying(hiddenDangerLevels, HiddenDangerLevelVO.class);
    }
}
