package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductModelVersion;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductModelVersionVO;

import java.util.List;
import java.util.Map;

/**
 * 物模型版本管理Service接口
 * 负责物模型版本快照的创建、查询、对比和回滚
 */
public interface V3ProductModelVersionService extends IService<MdProductModelVersion> {

    /**
     * 生成版本快照
     * 在创建或更新产品物模型时自动调用,保存完整JSON快照
     *
     * @param prodId 产品ID
     * @param changeType 变更类型: CREATE创建, UPDATE更新, ROLLBACK回滚
     * @param changeDesc 变更描述
     * @return 版本ID
     */
    Long createSnapshot(Long prodId, String changeType, String changeDesc);

    /**
     * 版本列表查询
     * 按产品和时间查询版本历史
     *
     * @param prodId 产品ID
     * @return 版本列表
     */
    List<ProductModelVersionVO> listVersions(Long prodId);

    /**
     * 版本详情查询
     * 返回完整的版本快照信息
     *
     * @param versionId 版本ID
     * @return 版本详情
     */
    ProductModelVersionVO getVersionDetail(Long versionId);

    /**
     * 版本对比
     * 对比两个版本的差异,返回新增、修改、删除的内容
     *
     * @param versionId1 版本1 ID
     * @param versionId2 版本2 ID
     * @return 差异对比结果
     */
    Map<String, Object> compareVersions(Long versionId1, Long versionId2);

    /**
     * 版本回滚
     * 恢复到指定版本,会创建新的版本记录(类型为ROLLBACK)
     *
     * @param prodId 产品ID
     * @param versionId 要回滚到的版本ID
     */
    void rollbackToVersion(Long prodId, Long versionId);

    /**
     * 获取产品的当前激活版本
     *
     * @param prodId 产品ID
     * @return 当前版本,如果不存在则返回null
     */
    MdProductModelVersion getCurrentVersion(Long prodId);
}
