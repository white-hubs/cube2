package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerCategoryCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command.HiddenDangerCategoryCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerCategoryMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenDangerCategoryParams;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerCategoryVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerCategory;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.StringUtils;
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
 * 隐患分类接口
 *
 * @author lzy
 * @date 2022/4/26
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerCategoryController {

    private final HiddenDangerCategoryCommandService hiddenDangerCategoryCommandService;

    private final HiddenDangerCategoryMapper hiddenDangerCategoryMapper;

    /**
     * 隐患分类新增
     *
     * @param command 新增参数
     */
    @PostMapping("/hidden-danger-category")
    public void save(@Valid @RequestBody HiddenDangerCategoryCommand command) {
        this.hiddenDangerCategoryCommandService.save(command);
    }

    /**
     * 隐患分类编辑
     *
     * @param id      主键
     * @param command 编辑参数
     */
    @PutMapping("/hidden-danger-category/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody HiddenDangerCategoryCommand command) {
        this.hiddenDangerCategoryCommandService.update(id, command);
    }

    /**
     * 隐患分类删除
     *
     * @param id 主键
     */
    @DeleteMapping("/hidden-danger-category/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.hiddenDangerCategoryCommandService.remove(id);
    }

    /**
     * 隐患分类列表
     *
     * @return 隐患分类信息
     */
    @GetMapping("/hidden-danger-category/list")
    public List<HiddenDangerCategoryVO> listHiddenDangerCategory(HiddenDangerCategoryParams params) {

        List<HiddenDangerCategory> hiddenDangerCategories = this.hiddenDangerCategoryMapper.selectList(Wrappers
                                                                                                               .<HiddenDangerCategory>lambdaQuery()
                                                                                                               .like(!StringUtils.isEmpty(params.getKeyword()), HiddenDangerCategory::getName, params.getKeyword()));
        return ObjectUtils.copying(hiddenDangerCategories, HiddenDangerCategoryVO.class);
    }
}
