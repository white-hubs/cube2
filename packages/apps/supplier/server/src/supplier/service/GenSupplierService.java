package com.guhecloud.cube.srv.gen.supplier.server.supplier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.entity.GenSupplier;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.GenSupplierModifyParam;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.GenSupplierQueryParam;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.UpdateDisableParam;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.vo.GenSupplierVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lzy
 * @date 2021/12/3
 */
public interface GenSupplierService extends IService<GenSupplier> {

    Long addSupplier(GenSupplierModifyParam param);

    void importSupplierTemplate(List<GenSupplierModifyParam> param);

    void updateSupplier(Long id, GenSupplierModifyParam param);

    Pagination<GenSupplierVO> pageSupplierDetail(GenSupplierQueryParam param);

    void updateDisable(@RequestBody List<UpdateDisableParam> updateDisableParams);

}
