package com.guhecloud.kaihe.scada.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.kaihe.scada.server.dto.CategoryDTO;
import com.guhecloud.kaihe.scada.server.entity.Category;

public interface CategoryService extends IService<Category> {

    void save(CategoryDTO categoryDTO);

    void update(CategoryDTO categoryDTO);

    void remove(String data);

}
