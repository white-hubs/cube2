package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

@Data
public class InspectRecordStandardItemVO {

    /**
     * 巡检项ID
     */
    private Long itemId;

    /**
     * 巡检项名称
     */
    private String itemName;

    /**
     * 要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)
     */
    private String itemImageResult;

    /**
     * 要填写备注的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)
     */
    private String remarkResult;

    /**
     * 巡检结果Id
     */
    private Long itemResultId;

    /**
     * 巡检项结果值
     */
    private String itemResultValue;

    /**
     * 巡检项结果名称
     */
    private String itemResultName;

    /**
     * 0 不是  1 是
     */
    private Boolean errorValueFlag;

    /**
     * 异常说明
     */
    private String errorDesc;

    /**
     * 文件集合
     */
    private List<FileVO> fileVOS;

    /**
     * 巡检项tag
     */
    private String itemTag;
}
