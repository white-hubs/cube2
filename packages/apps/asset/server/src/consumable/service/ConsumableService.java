package com.guhecloud.cube.srv.gen.property.management.server.consumable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.Consumable;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 描述：耗材管理服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
public interface ConsumableService extends IService<Consumable> {

    Long add(ConsumableParam param);

    void update(Long id, ConsumableParam param);

    void delete(Long id);

    ConsumableVO getById(Long id);

    Pagination<ConsumableVO> getPage(ConsumableQueryParam param);

    /**
     * 耗材入库
     *
     * @param param
     */
    Long addStock(ConsumableAddStockParam param);

    /**
     * 出库
     *
     * @param param
     */
    Long reduceStock(ConsumableReduceStockParam param);

    /**
     * 耗材导入
     */
    void importTemplate(MultipartFile file);

    /**
     * 导出耗材EXCEL模板
     */
    void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response);
}