package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.file;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TrafficDetectionAreaFileAppService {

    private final FileClient fileClient;

    private final FileRefClient fileRefClient;

    private static final String TRAFFIC_DETECTION_FILE_SCENE_CODE = "TRAFFIC_DETECTION_AREA::INFO";

    public TrafficDetectionAreaFileAppService(FileClient fileClient, FileRefClient fileRefClient) {
        this.fileClient = fileClient;
        this.fileRefClient = fileRefClient;
    }

    public void modifyTrafficDetectionAreaFiles(Long trafficDetectionAreaId, Set<Long> fileIds) {
        fileRefClient.removeByBusinessKey(trafficDetectionAreaId);
        if (CollectionUtils.isNotEmpty(fileIds)) {
            FileRefCreateParam param = new FileRefCreateParam();
            param.setFileIdList(new ArrayList<>(fileIds));
            param.setBusinessKey(trafficDetectionAreaId);
            param.setSceneCode(TRAFFIC_DETECTION_FILE_SCENE_CODE);
            fileRefClient.refInduce(param);
        }
    }

    public List<FileVO> getFilesByBusinessKey(Long businessKey) {
        return fileClient.getByBusinessKey(businessKey);
    }
}
