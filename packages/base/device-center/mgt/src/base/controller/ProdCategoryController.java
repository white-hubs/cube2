package com.guhecloud.cube.iot.mgt.base.controller;

import com.guhecloud.cube.iot.mgt.base.param.CommonDictItemModifyParam;
import com.guhecloud.cube.iot.mgt.base.param.CommonDictItemPageParam;
import com.guhecloud.cube.iot.mgt.base.service.impl.CommonDictItemService;
import com.guhecloud.cube.iot.mgt.base.service.impl.ProdCategoryService;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemTreeVo;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.cube.iot.mgt.base.bus.InitSystemCategoryDic;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/base/prod-category")
public class ProdCategoryController {

    @Resource
    private CommonDictItemService commonDictItemService;

    @Resource
    private ProdCategoryService prodCategoryService;

    @Resource
    private InitSystemCategoryDic initSystemCategoryDic;

    @GetMapping("/page")
    public Pagination<DictItemVo> page(CommonDictItemPageParam paging) {
        //产品品类
        paging.setDictId(InnerDicEnum.prod_category.id);

        return commonDictItemService.listRootByPage(paging);
    }

    @GetMapping("/{itemId}")
    public DictItemVo getProdCategory(@PathVariable("itemId") Long itemId) {
        return commonDictItemService.queryDictItemWithAssociated(itemId);
    }

    @PostMapping("")
    public void saveProdCategory(@RequestBody @NonNull CommonDictItemModifyParam param) {
        prodCategoryService.saveProdCategory(param);
    }

    @PutMapping("/{itemId}")
    public void updateProdCategory(@RequestBody @NonNull CommonDictItemModifyParam param, @PathVariable String itemId) {
        param.setId(Long.valueOf(itemId));
        prodCategoryService.updateProdCategory(param);
    }

    @DeleteMapping("/{itemId}")
    public void deleteProdCategory(@PathVariable("itemId") Long itemId) {
        prodCategoryService.deleteProdCategory(itemId);
    }

    /**
     * 产品品类树
     *
     * @return 产品品类树
     */
    @GetMapping("/prod-category-tree")
    public List<DictItemTreeVo> listProdCategoryTree(@RequestParam(required = false) Long dictItemId) {
        return commonDictItemService.listDictItemTree(InnerDicEnum.prod_category.id, dictItemId);
    }

    /**
     * 初始化当前租户默认（标准）品类
     */
    @PostMapping("/init-standard")
    public void initStandard() {
        Integer tenantId = SessionContextHolder.getRequester().getTenant();
        TenantDTO tenantDTO = new TenantDTO();
        tenantDTO.setCode(tenantId);
        initSystemCategoryDic.ensureTenantCategoryDic(tenantDTO);
    }
}
