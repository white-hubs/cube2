package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.BuildRecord;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.BuildRecordVO;

import java.util.ArrayList;
import java.util.List;

public final class BuildRecordConverts {

    private BuildRecordConverts() {
    }

    public static BuildRecordVO toVO(BuildRecord entity) {
        BuildRecordVO vo = new BuildRecordVO();
        vo.setId(entity.getId());
        vo.setTaskId(entity.getTaskId());
        vo.setDeployId(entity.getDeployId());
        vo.setArtifactName(entity.getArtifactName());
        vo.setArtifactUrl(entity.getArtifactUrl());
        vo.setStatus(entity.getStatus());
        vo.setDurationMs(entity.getDurationMs());
        vo.setErrorMessage(entity.getErrorMessage());
        vo.setFileUrl(entity.getFileUrl());
        vo.setDeployStatus(entity.getDeployStatus());
        vo.setReleaseStatus(entity.getReleaseStatus());
        vo.setCreateBy(entity.getCreateBy());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }

    public static List<BuildRecordVO> toVOList(List<BuildRecord> entities) {
        List<BuildRecordVO> result = new ArrayList<>();
        for (BuildRecord entity : entities) {
            result.add(toVO(entity));
        }
        return result;
    }

}
