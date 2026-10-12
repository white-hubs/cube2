package com.guhecloud.kaihe.scada.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.kaihe.scada.server.dto.CategoryDTO;
import com.guhecloud.kaihe.scada.server.entity.Category;
import com.guhecloud.kaihe.scada.server.entity.Diagram;
import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceDataException;
import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceException;
import com.guhecloud.kaihe.scada.server.mapper.CategoryMapper;
import com.guhecloud.kaihe.scada.server.mapper.DiagramMapper;
import com.guhecloud.kaihe.scada.server.service.CategoryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final DiagramMapper diagramMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void save(CategoryDTO categoryDTO) {
        categoryDTO.setProjectId(SessionContextHolder.getRequester().getTenant());
        if (categoryMapper.selectOne(Wrappers.<Category>lambdaQuery().eq(Category::getName, categoryDTO.getName())
                                             .eq(Category::getProjectId, categoryDTO.getProjectId())) != null) {
            Map<String, String> message = new HashMap<>();
            message.put("AreaName", "名称已经存在");
            throw new ServiceDataException(message);
        }
        categoryMapper.insert(categoryDTO.convert());
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        categoryDTO.setProjectId(SessionContextHolder.getRequester().getTenant());
        Category categoryByName = categoryMapper.selectOne(Wrappers.<Category>lambdaQuery().eq(Category::getName, categoryDTO.getName())
                                                                   .eq(Category::getProjectId, categoryDTO.getProjectId()));
        if (categoryByName != null && !Objects.equals(categoryDTO.getID(), categoryByName.getID())) {
            Map<String, String> message = new HashMap<>();
            message.put("AreaName", "名称已经存在");
            throw new ServiceDataException(message);
        }
        categoryMapper.updateById(categoryDTO.convert());
    }

    @Override
    public void remove(String data) {
        List<Map<String, Integer>> list;
        try {
            list = objectMapper.readValue(data, List.class);
        } catch (Exception e) {
            throw new ServiceException("数据格式错误");
        }
        for (int i = 0; i < list.size(); i++) {
            Integer id = list.get(i).get("ID");
            if (diagramMapper.selectList(Wrappers.<Diagram>lambdaQuery().eq(Diagram::getCategory, id)).size() > 0) {
                throw new ServiceException("该目录下有图形不能删除,请先删除图形");
            }
            categoryMapper.deleteById(id);
        }
    }

}
