package com.guhecloud.cube.srv.gen.park.management.server.commandgateway;

import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileCommonApi {

    private final FileRefClient fileRefClient;

    /**
     * 保存文件
     *
     * @param id      业务ID
     * @param fileIds 文件ID
     */
    public void saveFiles(Long id, List<Long> fileIds) {
        List<FileRefCreateParam> params = Optional.ofNullable(fileIds).orElse(Collections.emptyList())
                .stream().map(fileId -> {
                    FileRefCreateParam param = new FileRefCreateParam();
                    param.setFileId(fileId);
                    param.setBusinessKey(id);
                    return param;
                }).collect(Collectors.toList());

        if (params.size() > 0) {
            this.fileRefClient.saveBatch(params);
        }
    }

    public void removeFilesByBusinessKey(Long businessKey) {
        this.fileRefClient.removeByBusinessKey(businessKey);
    }
}
