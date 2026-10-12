package com.guhecloud.cube.srv.gen.safety.education.server.transfer.constant;

import lombok.Getter;

/**
 * 消息主题
 *
 * @author : appet
 * @date : 2023-4-20
 */
@Getter
public class TransferTopicConstant {

    /**
     * 集团培训任务主题
     */
    public static final String CULTIVATE_TASK_TOPIC = "HEADQUARTERS_CULTIVATE_TASK";

    /**
     * 集团考试任务主题
     */
    public static final String EXAM_TASK_TOPIC = "HEADQUARTERS_EXAM_TASK";

    /**
     * 市场消费市场消费组
     */
    public static final String MARKET_PUSH_EXAM_GROUP = "MARKET_PUSH_EXAM_GROUP";
    public static final String MARKET_PUSH_EXAM_MISS_GROUP = "MARKET_PUSH_EXAM_MISS_GROUP";
    public static final String MARKET_PUSH_TRAIN_GROUP = "MARKET_PUSH_TRAIN_GROUP";
    public static final String MARKET_PUSH_TRAIN_UN_SIGN_GROUP = "MARKET_PUSH_TRAIN_NO_SIGN_GROUP";
    /**
     * 集团消费市场消费组
     */
    public static final String GROUP_RECEIVE_EXAM_GROUP = "GROUP_RECEIVE_EXAM_GROUP";
    public static final String GROUP_RECEIVE_EXAM_MISS_GROUP = "GROUP_RECEIVE_EXAM_MISS_GROUP";
    public static final String GROUP_RECEIVE_TRAIN_GROUP = "GROUP_RECEIVE_TRAIN_GROUP";
    public static final String GROUP_RECEIVE_TRAIN_UN_SIGN_GROUP = "GROUP_RECEIVE_TRAIN_UN_SIGN_GROUP";

    public static final String GROUP_RECEIVE_EXAM_TOP = "HEAD_MARKET_USER_EXAM_SUBMITTED";

    public static final String GROUP_RECEIVE_EXAM_MISS_TOP = "HEAD_MARKET_USER_EXAM_MISS";

    public static final String GROUP_RECEIVE_TRAIN_TOP = "HEAD_MARKET_CULTIVATE_USER_SIGN";

    public static final String GROUP_RECEIVE_TRAIN_UN_SIGN_TOP = "HEAD_MARKET_CULTIVATE_USER_UN_SIGN";

    private TransferTopicConstant() {
    }
}
