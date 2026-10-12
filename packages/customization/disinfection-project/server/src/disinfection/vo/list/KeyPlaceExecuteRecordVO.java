package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * 重点场所执行记录返回对象
 */
@Data
public class KeyPlaceExecuteRecordVO {
    private Long id;
    /**
     * 重点场所id
     */
    private Long keyPlaceId;
    /**
     * 场所名称
     */
    private String placeName;

    /**
     * 场所分类ID
     */
    private Long placeTypeId;

    /**
     * 场所分类名称
     */
    private String placeTypeName;

    /**
     * 场所地址
     */
    private String placeAddress;
    /**
     * 执行人id
     */
    private Long executorId;
    private String executorName;
    /**
     * 描述
     */
    private String taskDesc;
    List<Long> fileIdList;
    /**
     * 文件
     */
    private List<FileVO> fileVOList;
}
