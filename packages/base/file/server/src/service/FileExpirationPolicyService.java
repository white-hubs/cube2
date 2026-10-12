package com.guhecloud.cube.base.file.server.service;

import com.guhecloud.cube.base.file.server.dto.FileExpirationPolicyDTO;
import com.guhecloud.cube.base.file.server.param.FileExpirationPolicyPageParam;
import com.guhecloud.cube.base.file.server.vo.FileExpirationPolicyVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 根据文件过期策略删除文件
 */
public interface FileExpirationPolicyService {
    Pagination<FileExpirationPolicyVO> getExpirationPolicyPage(FileExpirationPolicyPageParam param);

    Boolean addExpirationPolicy(FileExpirationPolicyDTO param);

    Boolean updateExpirationPolicy(FileExpirationPolicyDTO param);

    Boolean deleteExpirationPolicy(Long id);

    /**
     * 清理文件
     */
    void scheduledDelete();
}
