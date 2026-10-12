package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 培训人员情况统计
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateUserStatVO extends CultivateCommonStatVO {
    private int trainNum = 0;//培训人数
    private int signedNum = 0;//到勤人数
    private int unsignedNum = 0;//缺勤人数
    private int belatedNum = 0;//迟到人数
}