package com.guhecloud.platform.dcm.support;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.platform.dcm.Identifiable;

import java.util.Collection;

/**
 * 树形结构的DcmHelper需要支持的能力。
 *
 * @param <BE>
 * @param <DE>
 */
public interface ITreeDcmHelper<BE, DE extends Identifiable> extends IDcmHelper<BE, DE> {

    /**
     * 使用传入的service 对一组Dcm实体进行保存。
     * 从性能角度出发，实现需尽可能进行批量保存。但可留作优化项目。
     */
    void treeSaveBatch(IService<BE> service, Collection<DE> entities);
}
