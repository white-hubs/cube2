package com.guhecloud.cube.srv.gen.property.management.server.property.util;

import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FilesUtil {
    public static List<FileRefCreateParam> buildFiles(Long id, Collection<Long> fileIds) {
        if (!CollectionUtils.isEmpty(fileIds)) {
            return fileIds.stream().map(fileId -> {
                FileRefCreateParam param = new FileRefCreateParam();
                param.setFileIdStr(fileId.toString());
                param.setBusinessKey(id);
                return param;
            }).collect(Collectors.toList());
        }
        return null;
    }
}
