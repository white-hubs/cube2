package com.guhecloud.cube.deliver.project.merchant.utils;

import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FilesUtil {
    public static List<FileRefCreateParam> buildFiles(String id, Collection<Long> fileIds) {
        if (!CollectionUtils.isEmpty(fileIds)) {
            List<FileRefCreateParam> params = fileIds.stream().map(fileId -> {
                FileRefCreateParam param = new FileRefCreateParam();
                param.setFileId(fileId);
                param.setBusinessKey(Long.parseLong(id));
                return param;
            }).collect(Collectors.toList());
            return params;
        }
        return null;
    }
}
