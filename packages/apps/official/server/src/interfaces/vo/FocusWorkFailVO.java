package com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项未完成回复
 */
@Data
public class FocusWorkFailVO {

    private Long id;

    private Long focusWorkId;

    private String failReason;

    private String fileIds;

    private List<FileVO> fileList;

}