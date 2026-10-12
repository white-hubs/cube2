package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.GoodsCategoryListDTO;
import com.guhecloud.cube.biz.mdata.client.dto.GoodsCategorySyncDTO;
import com.guhecloud.cube.biz.mdata.client.param.GoodsCategoryBatchRenewParam;
import com.guhecloud.cube.biz.mdata.client.param.GoodsCategoryCreateParam;
import com.guhecloud.cube.biz.mdata.client.param.GoodsCategoryIdsParam;
import com.guhecloud.cube.biz.mdata.client.param.GoodsCategoryQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "mdata")
public interface GoodsCategoryClient {

    @GetMapping("/goods-category/sequence/list")
    List<String> getSequences(@SpringQueryMap GoodsCategoryQueryParam queryParam);

    @PostMapping("/mall-goods-category/list")
    List<GoodsCategoryListDTO> mallCategoryList(@RequestBody GoodsCategoryQueryParam queryParam);

    @PostMapping("/mall-goods-category/ids")
    List<String> getByIdAndLevel(@RequestBody GoodsCategoryIdsParam param);

    @PostMapping("/get-name-by-ids/ids")
    List<String> getNameByIds(@RequestBody List<String> ids);

    /**
     * 通过Code查询 品类的Sequence
     *
     * @param code
     *
     * @return
     */
    @GetMapping("/goods-category/{code}/sequence")
    String getSequenceByCode(@PathVariable("code") String code);

    /**
     * 删除租户下的所有品类
     */
    @DeleteMapping("/goods-category/all")
    void removeAll();

    /**
     * 插入品类
     *
     * @param param
     */
    @PostMapping("/goods-category")
    void save(@RequestBody GoodsCategoryCreateParam param);

    @GetMapping("/goods-category/all/list")
    List<GoodsCategorySyncDTO> getAllList(@SpringQueryMap GoodsCategoryQueryParam queryParam);

    /**
     * 商品品类批量更新
     * 根据编码 相同就更新，不同就新增
     *
     * @param param 品类
     */
    @PostMapping("/third/goods-category/batchRenew")
    void thirdCategoryBatchRenew(@RequestBody GoodsCategoryBatchRenewParam param);
}
