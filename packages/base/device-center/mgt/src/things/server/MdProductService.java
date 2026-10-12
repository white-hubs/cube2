package com.guhecloud.cube.iot.mgt.things.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.things.model.MdProduct;
import com.guhecloud.cube.iot.mgt.things.param.ProdDetailQueryParam;
import com.guhecloud.cube.iot.mgt.things.param.ProdFetchFullParam;
import com.guhecloud.cube.iot.mgt.things.param.ProductPageParam;
import com.guhecloud.cube.iot.mgt.things.vo.ProdNameAndNodeTypeVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface MdProductService extends IService<MdProduct> {

    void saveProduct(ProductVo param);

    void updateProduct(ProductVo param);

    void deleteProduct(Long prodId);

    ProductVo productDetail(Long prodId);

    Pagination<ProductVo> listByPage(ProductPageParam paging);

    void updateStatus(Long prodId, Integer status);

    /**
     * 获取产品类型
     *
     * @return 产品类型
     */
    List<DictItemVo> mdProductService(Integer prodNodeType);

    /**
     * 获取产品信息
     *
     * @param fetchFullParam 产品筛选参数
     *
     * @return 产品信息
     */
    Pagination<ProductVo> fetchFullProd(ProdFetchFullParam fetchFullParam);

    /**
     * 产品详情<br/>
     * 定制化查询
     *
     * @param param 查询参数
     *
     * @return 产品详情
     */
    ProductVo productDetail(ProdDetailQueryParam param);

    /**
     * 产品名称和产品节点类型
     *
     * @param productId 产品id
     *
     * @return 产品名称和产品类型
     */
    ProdNameAndNodeTypeVo queryProdNameAndNodeType(Long productId);

    /**
     * 获取所有产品<br/>
     * listAll 进提供id和名称,性能无法满足
     *
     * @return 所有产品
     */
    List<ProductVo> listAll();

    /**
     * 判断一个产品是不是网关
     *
     * @param productId productId
     *
     * @return true/false
     */
    boolean isGateway(Long productId);

}
