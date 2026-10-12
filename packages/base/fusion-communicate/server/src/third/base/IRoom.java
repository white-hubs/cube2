package com.guhecloud.cube.biz.fusion.communicate.server.third.base;

import java.util.Set;

public interface IRoom {

    /**
     * 创建房间
     *
     * @return 房间号
     */
    String createRoom();

    /**
     * 加入房间
     *
     * @param roomNumber 房间号
     * @param userIds    用户id
     */
    void joinConferenceRoom(String roomNumber, Set<Long> userIds);

    /**
     * 离开房间
     *
     * @param roomNumber 房间号
     * @param userIds    用户
     */
    void leaveConferenceRoom(String roomNumber, Set<Long> userIds);

    /**
     * 踢出房间
     *
     * @param roomNumber 房间号
     * @param userIds    用户
     */
    void removeConferenceRoomUsers(String roomNumber, Set<Long> userIds);

    /**
     * 解散房间
     *
     * @param roomNumber 房间号
     */
    void disbandRoomRequest(String roomNumber);
}
