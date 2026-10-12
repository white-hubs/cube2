package com.guhecloud.cube.iot.mgt.transfer.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 导入结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResult {

    private ImportState productSuccess;

    private ImportState deviceSuccess;

    /**
     * 产品失败记录详情
     */
    private List<String> productFailDetails;

    /**
     * 设备失败记录详情
     */
    private List<String> deviceFailDetails;

    /**
     * 导入时间戳
     */
    private Long timestamp;

    /**
     * 整体失败
     * @param msg 失败消息
     * @return 导入结果
     */
    public static ImportResult failureAll(String msg) {
        return ImportResult.builder()
                .productSuccess(ImportState.ALL_FAIL)
                .deviceSuccess(ImportState.ALL_FAIL)
                .productFailDetails(Collections.singletonList(msg))
                .deviceFailDetails(Collections.singletonList(msg))
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static ImportResult successAll(String msg) {
        return ImportResult.builder()
                .productSuccess(ImportState.ALL_SUCCESS)
                .deviceSuccess(ImportState.ALL_SUCCESS)
                .productFailDetails(Collections.singletonList(msg))
                .deviceFailDetails(Collections.singletonList(msg))
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public enum ImportState {
        //全部成功
        ALL_SUCCESS,
        //部分成功
        PART_SUCCESS,
        //全部失败
        ALL_FAIL
    }
}