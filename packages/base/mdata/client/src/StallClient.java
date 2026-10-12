package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.DbcStallDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallCategoryDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDetailDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallIdTypeDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallStatDTO;
import com.guhecloud.cube.biz.mdata.client.param.StallCategoryCreateBatchParam;
import com.guhecloud.cube.biz.mdata.client.param.StallCategoryQueryParam;
import com.guhecloud.cube.biz.mdata.client.param.StallCreateBathParam;
import com.guhecloud.cube.biz.mdata.client.param.StallCreateParam;
import com.guhecloud.cube.biz.mdata.client.param.StallMasterOtherParam;
import com.guhecloud.cube.biz.mdata.client.param.StallPostQueryParam;
import com.guhecloud.cube.biz.mdata.client.param.StallQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author lzy
 * @date 2021/10/19
 */
@FeignClient(name = "mdata")
public interface StallClient {

    @GetMapping("/stall/id")
    StallDTO getStallById(@RequestParam("id") Long id);

    @GetMapping("/stall/ids")
    List<StallDTO> getStallByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("/stall/list/ids")
    List<StallDTO> getStallListByIds(@RequestParam("ids") List<Long> ids);

    @PostMapping("/stall/list/post/ids")
    List<StallDTO> postStallListByIds(@RequestBody StallPostQueryParam param);

    @GetMapping("/stall/list")
    List<StallDTO> getStallList();

    /**
     * 档位分页查询（内部调用）
     *
     * @param queryParam 档位查询参数
     *
     * @return 档位分页集合
     */
    @GetMapping("/stall-client/page")
    Pagination<StallDTO> getStallPage(@SpringQueryMap StallQueryParam queryParam);

    @PostMapping("/post/stall/page")
    Pagination<StallDTO> postStallPage(@RequestBody StallQueryParam queryParam);

    @GetMapping("/stall/{id}")
    StallDetailDTO getDetailById(@PathVariable("id") Long id);

    /**
     * 档位类型查询（内部调用）
     *
     * @param ids
     *
     * @return
     */
    @GetMapping("/stall-category/ids")
    List<StallCategoryDTO> listStallCategory(@RequestParam("ids") List<Long> ids);

    /**
     * 档位分类列表查询
     *
     * @return 档位分类列表集合
     */
    @GetMapping("/stall-category/list")
    List<StallCategoryDTO> listStallCategory(@SpringQueryMap StallCategoryQueryParam queryParam);

    /**
     * 根据档位ID 查询对应商户下的所有档位
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/stall/{id}/stalls")
    List<StallDTO> getStallsInMerchantById(@PathVariable("id") Long id);

    /**
     * 统计一级区域下的档位数量
     *
     * @return
     */
    @GetMapping("/stall/area/stat")
    List<StallStatDTO> getStallStatByArea();

    /**
     * 插入档位
     *
     * @param param
     */
    @PostMapping("/stall/sync")
    void save(@RequestBody StallCreateParam param);

    /**
     * 通过code查询档位
     *
     * @param code
     *
     * @return
     */
    @GetMapping("/stall/code/{code}")
    StallDTO getStallByCode(@PathVariable("code") String code);

    /**
     * 查询档位类型
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/stall-category/list")
    List<StallCategoryDTO> getStallCategorylist(@SpringQueryMap StallCategoryQueryParam queryParam);

    /**
     * 大白菜挡位类型批量新增
     * 如果存在相同名称挡位跳过
     * 注意：不会新增文件信息
     *
     * @param param 挡位类型集合入参
     */
    @PostMapping("/stall-category/saveBatchOnlyEntity")
    void saveBathOnlyEntity(@RequestBody StallCategoryCreateBatchParam param);

    /**
     * 大白菜挡位批量同步
     * 因为同步数据时，第三方可能没有相关的参数
     *
     * @param param 挡位入参集合
     */
    @PostMapping("/stall/saveOrUpdateBatch")
    void saveOrUpdateBatch(@RequestBody StallCreateBathParam param);

    /**
     * 获取挡位数据(完整PO信息)
     *
     * @return 挡位集合
     */
    @GetMapping("/stall/getAllStallEntity")
    List<DbcStallDTO> getAllStallEntity();

    /**
     * 通过名称查询档位
     *
     * @param name
     *
     * @return
     */
    @GetMapping("/stall/stallName")
    StallDTO getStallByName(@RequestParam("name") String name);

    /**
     * 获取档位ID列表
     *
     * @param queryParam 查询参数
     *
     * @return 档位ID,type列表
     */
    @PostMapping("/stall/idTypes")
    List<StallIdTypeDTO> getStallIdTypes(@RequestBody @Valid StallQueryParam queryParam);

    /**
     * 根据区域id列表批量获取档位数统计
     *
     * @param areaIds 区域id列表
     *
     * @return 区域id与档位数的映射，key为区域id，value为档位数
     */
    @PostMapping("/stall/count/by/areaIds")
    Map<Long, Long> countByAreaIds(@RequestBody List<Long> areaIds);

    /**
     * 获取某档位档主的其他档位
     * 注意不包含自己
     *
     * @param param 条件
     *
     * @return 其他档位列表
     */
    @PostMapping("/stall/stallMasterOthers")
    List<StallDTO> postStallMasterOthers(@RequestBody StallMasterOtherParam param);

}
