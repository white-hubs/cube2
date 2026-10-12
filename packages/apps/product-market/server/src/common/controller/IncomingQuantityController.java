package com.guhecloud.cube.srv.gen.product.market.server.common.controller;

import com.guhecloud.cube.srv.gen.product.market.server.common.param.IncomingQuantityParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.IncomingQuantityQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.IncomingQuantityService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.IncomingQuantityVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 农产品来货量
 * @date 2023-05-10 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncomingQuantityController {
    private final IncomingQuantityService incomingQuantityService;

    /**
     * 功能描述： 农产品来货量添加
     */
    @PostMapping(value = "/incoming-quantity")
    public Long add(@Valid @RequestBody IncomingQuantityParam param) {
        return incomingQuantityService.add(param);
    }

    /**
     * 功能描述： 农产品来货量修改
     */
    @PutMapping(value = "/incoming-quantity/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody IncomingQuantityParam param) {
        incomingQuantityService.update(id, param);
    }

    /**
     * 功能描述： 农产品来货量删除
     */
    @DeleteMapping(value = "/incoming-quantity/{id}")
    public void delete(@PathVariable Long id) {
        incomingQuantityService.delete(id);
    }

    /**
     * 功能描述： 农产品来货量批量删除
     */
    @DeleteMapping(value = "/incoming-quantity/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        incomingQuantityService.batchDelete(ids);
    }

    /**
     * 功能描述： 农产品来货量分页
     */
    @GetMapping(value = "/incoming-quantity/page")
    public Pagination<IncomingQuantityVO> getPage(IncomingQuantityQueryParam param) {
        return incomingQuantityService.getPage(param);
    }

    /**
     * 功能描述： 农产品来货量详情
     */
    @GetMapping(value = "/incoming-quantity/{id}")
    public IncomingQuantityVO getById(@PathVariable Long id) {
        return incomingQuantityService.getById(id);
    }

}