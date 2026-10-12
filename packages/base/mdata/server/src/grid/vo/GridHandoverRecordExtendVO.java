package com.guhecloud.cube.biz.mdata.server.grid.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * 网格交接扩展信息
 */
@Data
public class GridHandoverRecordExtendVO {
    private String attInfo1;
    private List<FileVO> file1;
    private String attInfo2;
    private List<FileVO> file2;
    private String attInfo3;
    private List<FileVO> file3;
    private String attInfo4;
    private List<FileVO> file4;
}
