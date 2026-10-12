package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.entity.ProductLine;
import com.guhecloud.cube.biz.ouaa.server.param.ProductLineModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ProductLineQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ProductLineService;
import com.guhecloud.cube.biz.ouaa.server.vo.ProductLinePageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ProductLineVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
@Deprecated
public class ProductLineController {

    private final ProductLineService productLineService;

    @PostMapping("/product-line")
    public Long save(@Valid @RequestBody ProductLineModifyParam param) {
        return productLineService.save(param);
    }

    @PutMapping("/product-line/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody ProductLineModifyParam param) {
        productLineService.update(id, param);
    }

    @DeleteMapping("/product-line/{id}")
    public void remove(@PathVariable("id") Long id) {
        productLineService.remove(id);
    }

    @GetMapping("/product-lines")
    public Pagination<ProductLinePageVO> listByPage(@Valid ProductLineQueryParam param) {
        Page<ProductLine> page = this.productLineService.listByPage(param);
        List<ProductLinePageVO> pageVOS = ObjectUtils.copying(page.getRecords(), ProductLinePageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, pageVOS);
    }

    @GetMapping("/product-line/{id}")
    public ProductLineVO getById(@PathVariable("id") Long id) {
        ProductLine productLine = this.productLineService.getById(id);
        Assert.isNotFound(productLine, "不存在产品线信息");
        return ObjectUtils.copying(productLine, ProductLineVO.class);
    }

}
