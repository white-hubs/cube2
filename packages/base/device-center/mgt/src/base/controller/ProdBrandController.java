package com.guhecloud.cube.iot.mgt.base.controller;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.iot.mgt.base.param.CommonDictItemPageParam;
import com.guhecloud.cube.iot.mgt.base.param.ProdBrandModifyParam;
import com.guhecloud.cube.iot.mgt.base.service.impl.CommonDictItemService;
import com.guhecloud.cube.iot.mgt.base.service.impl.ProdBrandService;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
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
import org.springframework.web.bind.annotation.RestController;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/base/prod-brand")
public class ProdBrandController {

    @Resource
    private CommonDictItemService commonDictItemService;

    @Resource
    private ProdBrandService prodBrandService;
    @Resource
    private FileClient fileClient;

    @GetMapping("/page")
    public Pagination<DictItemVo> page(CommonDictItemPageParam paging) {
        //产品品牌
        paging.setDictId(InnerDicEnum.prod_brand.id);

        Pagination<DictItemVo> page = commonDictItemService.listRootByPage(paging);

        //图片
        for (DictItemVo dictItemVo : page.getRecords()) {
            if (StringUtils.isNoneBlank(dictItemVo.getItemValue())) {

                dictItemVo.setFileVOs(fileClient.getByBusinessKey(dictItemVo.getId()));
            }
        }

        return page;
    }

    @PostMapping("")
    public void saveProdBrand(@RequestBody @NonNull ProdBrandModifyParam param) {
        prodBrandService.saveProdBrand(param);
    }

    @PutMapping("/{itemId}")
    public void updateProdBrand(@RequestBody @NonNull ProdBrandModifyParam param, @PathVariable String itemId) {
        param.setId(Long.valueOf(itemId));
        prodBrandService.updateProdBrand(param);
    }

    @DeleteMapping("/{itemId}")
    public void deleteProdBrand(@PathVariable("itemId") Long itemId) {
        prodBrandService.deleteProdBrand(itemId);
    }

    @GetMapping("/{itemId}")
    public DictItemVo getProdBrand(@PathVariable("itemId") Long itemId) {
        return commonDictItemService.queryDictItemWithAssociated(itemId);
    }
}
