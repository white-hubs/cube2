package com.guhecloud.cube.base.file.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-12  16:39
 * @Description:
 * @Version: 1.0
 */
@Data
public class FileExpirationPolicyPageParam extends Paging {
    private String sceneCode;
}
