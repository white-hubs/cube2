package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo;

import lombok.Data;

import java.util.List;

/**
 * 机组导入结果
 */
@Data
public class ImportResult {
    private Integer fail;
    private Integer success;
    List<KevValueString> errorMsgList;
}
