package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerTypeCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command.HiddenDangerTypeCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerTypeVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerCategory;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerCategoryQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.query.HiddenDangerTypeCollectionQuery;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 隐患分类接口
 *
 * @author lzy
 * @date 2022/4/26
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerTypeController {

    private final HiddenDangerTypeCommandService hiddenDangerTypeCommandService;
    private final HiddenDangerCategoryQueryService hiddenDangerCategoryQueryService;
    private final HiddenDangerTypeQueryService hiddenDangerTypeQueryService;

    /**
     * 隐患类别新增
     *
     * @param command 新增参数
     */
    @PostMapping("/hidden-danger-type")
    public void save(@Valid @RequestBody HiddenDangerTypeCommand command) {
        this.hiddenDangerTypeCommandService.save(command);
    }

    /**
     * 隐患类别编辑
     *
     * @param id      主键
     * @param command 编辑参数
     */
    @PutMapping("/hidden-danger-type/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody HiddenDangerTypeCommand command) {
        this.hiddenDangerTypeCommandService.update(id, command);
    }

    /**
     * 隐患类别删除
     *
     * @param id 隐患类别主键
     */
    @DeleteMapping("/hidden-danger-type/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.hiddenDangerTypeCommandService.remove(id);
    }

    /**
     * 隐患类别列表
     *
     * @return 隐患类别信息
     */
    @GetMapping("/hidden-danger-type/list")
    public List<HiddenDangerTypeVO> listHiddenDangerType(HiddenDangerTypeCollectionQuery query) {
        // 获取隐患类别信息
        List<HiddenDangerType> hiddenDangerTypes = this.hiddenDangerTypeQueryService.listHiddenDangerType(query);
        if (CollectionUtils.isEmpty(hiddenDangerTypes)) {
            return Collections.emptyList();
        }

        // 获取隐患分类信息
        Set<Long> hiddenDangerCategoryIds = hiddenDangerTypes.stream().map(HiddenDangerType::getHiddenDangerCategoryId)
                .collect(Collectors.toSet());
        List<HiddenDangerCategory> hiddenDangerCategories = this.hiddenDangerCategoryQueryService.listByIds(hiddenDangerCategoryIds);
        Map<Long, List<HiddenDangerCategory>> hiddenDangerCategoryMap =
                hiddenDangerCategories.stream().collect(Collectors.groupingBy(HiddenDangerCategory::getId));

        // 构建信息
        List<HiddenDangerTypeVO> records = ObjectUtils.copying(hiddenDangerTypes, HiddenDangerTypeVO.class);
        records.forEach(record -> {
            if (hiddenDangerCategoryMap.containsKey(record.getHiddenDangerCategoryId())) {
                HiddenDangerCategory hiddenDangerCategory = hiddenDangerCategoryMap.get(record.getHiddenDangerCategoryId()).get(0);
                record.setHiddenDangerCategoryName(hiddenDangerCategory.getName());
            }
        });
        return records;
    }

}
