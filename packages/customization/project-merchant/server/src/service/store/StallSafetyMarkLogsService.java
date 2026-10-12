package com.guhecloud.cube.deliver.project.merchant.service.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallSafetyMarkLogsDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyMarkLogs;

/**
 * <p>
 * 档位安全生产分值修改记录 服务类
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-27
 */
public interface StallSafetyMarkLogsService extends IService<StallSafetyMarkLogs> {

    /**
     * 新增一条修改安全分的记录，并且修改分数
     * @param dto 修改分数的记录DTO
     * @return 修改分数记录的ID
     */
    String save(StallSafetyMarkLogsDTO dto);

    /**
     * 删除一条修改安全分的记录，并且将分数恢复
     * @param id ID
     */
    void remove(String id);
}
