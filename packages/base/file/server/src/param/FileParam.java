package com.guhecloud.cube.base.file.server.param;

import lombok.Data;

import java.util.List;

@Data
public class FileParam {
    /**
     * 文件id集合
     */
    private List<Long> ids;

    /**
     * 多个文件id逗号拼接
     */
    private String fileIdStr;
}
