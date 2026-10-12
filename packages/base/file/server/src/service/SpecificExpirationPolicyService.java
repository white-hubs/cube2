package com.guhecloud.cube.base.file.server.service;

import com.guhecloud.cube.base.file.server.entity.FileExpirationPolicy;

public interface SpecificExpirationPolicyService {
    /**
     * 文件删除，根据过期策略配置的延时时间将逻辑删除的文件进行物理删除
     *
     * @param policy 过期策略
     */
    void basedTime(FileExpirationPolicy policy);

    /**
     * 文件标记删除，根据过期策略限定的删除时间进行逻辑删除
     *
     * @param policy 过期策略
     */
    void basedTimeTag(FileExpirationPolicy policy);

    /**
     * 基于文件大小
     *
     * @param policy 过期策略
     */
    void basedSize(FileExpirationPolicy policy);

    /**
     * 基于使用频率
     *
     * @param policy 过期策略
     */
    void basedFrequency(FileExpirationPolicy policy);
    /**标记临时文件*/
    /**清理临时文件*/

}
