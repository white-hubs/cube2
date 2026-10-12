package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

/**
 * 海康门禁远程核验结果。
 *
 * @param serialNo 对应请求事件流水号
 * @param checkResult 核验结果
 * @param info 门禁显示提示语
 * @param audioPrompt 门禁语音提示语
 */
public record HikRemoteCheckResultResponse(
        Integer serialNo,
        String checkResult,
        String info,
        String audioPrompt
) {
}
