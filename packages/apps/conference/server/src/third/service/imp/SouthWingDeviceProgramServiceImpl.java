package com.guhecloud.cube.srv.gen.conferencemanage.server.third.service.imp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.ConferenceMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.Conference;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.RoomScreenConfig;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.param.SouthWingProgramBaseParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.param.SouthWingProgramInsertParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.param.SouthWingProgramSendParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.response.ResultEntity;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.sdk.BaseSupport;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.service.DeviceProgramSendService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.util.HttpClientUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.guhecloud.cube.srv.gen.conferencemanage.server.third.service.imp.DeviceProgramSendServiceImpl.REQUEST_HEADER_ORIGIN;
import static com.guhecloud.cube.srv.gen.conferencemanage.server.third.service.imp.DeviceProgramSendServiceImpl.ROOM_SCREEN_URL;

/**
 * 南翼会议屏控制
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/14 14:35
 * @Description:
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SouthWingDeviceProgramServiceImpl implements DeviceProgramSendService {
    private final ConferenceMapper conferenceMapper;
    //状态码
    private final String SUCCESS_CODE = "0";

    @Override
    public void newProgram(RoomScreenConfig screenConfig, HttpServletRequest httpServletRequest) {

        List<NameValuePair> nameValuePairs = BaseSupport.buildBaseRequestMap(BaseSupport.INSERT_PROGRAM, screenConfig.getAuthInfo());

        SouthWingProgramInsertParam programInsertParam = new SouthWingProgramInsertParam();
        String programId = UUID.randomUUID().toString();
        programInsertParam.setId(programId);
        programInsertParam.setType("container");
        programInsertParam.setLayout("absolute");
        programInsertParam.setWidth("1920");
        programInsertParam.setHeight("1080");
        programInsertParam.setHomeId("");
        programInsertParam.setResourceId("");
        programInsertParam.setElements(Collections.singletonList(SouthWingProgramInsertParam
                                                                         .buildSouthWingProgramElement(buildSouthWingProgramWebItem(screenConfig, httpServletRequest))));

        Conference conference = conferenceMapper.selectById(screenConfig.getConferenceId());
        SouthWingProgramBaseParam southWingProgramBaseParam = new SouthWingProgramBaseParam();
        southWingProgramBaseParam.setName(Objects.nonNull(conference) ? conference.getName() : "会议室预约节目");
        southWingProgramBaseParam.setJson(JsonUtils.serialize(programInsertParam));
        southWingProgramBaseParam.setState("n");
        southWingProgramBaseParam.setCategoryId("18389a46-9223-474e-a15a-01f212203ce7");
        nameValuePairs.add(new BasicNameValuePair("programData", JsonUtils.serialize(southWingProgramBaseParam)));
        String result = HttpClientUtils.doPostForm(screenConfig.getProgramUrl(), null, nameValuePairs);
        ResultEntity response = JsonUtils.deserialize(result, ResultEntity.class);
        if (!SUCCESS_CODE.equals(response.getErrorCode())) {
            throw new ServiceException("下发节目失败:" + response.getErrorMsg());
        }
        Map<String, String> resultMap = JsonUtils.deserialize(response.getData(), new TypeReference<Map<String, String>>() {
        });
        screenConfig.setProgramId(resultMap.get("id"));
    }

    @Override
    public void clearProgram(RoomScreenConfig screenConfig) {
        List<NameValuePair> nameValuePairs = BaseSupport.buildBaseRequestMap(BaseSupport.DELETED_PROGRAM, screenConfig.getAuthInfo());
        String ids = JsonUtils.serialize(Collections.singletonList(screenConfig.getProgramId()));
        nameValuePairs.add(new BasicNameValuePair("ids", ids));
        String result = HttpClientUtils.doPostForm(screenConfig.getProgramUrl(), null, nameValuePairs);
        ResultEntity response = JsonUtils.deserialize(result, ResultEntity.class);
        if (!SUCCESS_CODE.equals(response.getErrorCode())) {
            throw new ServiceException("清空节目失败:" + response.getErrorMsg());
        }
    }

    @Override
    public void sendProgram(BookRoom bookRoom, RoomScreenConfig roomScreenConfig) {
        if (roomScreenConfig == null) {
            throw new ServiceException("未找到会议屏配置信息");
        }
        Conference conference = conferenceMapper.selectById(roomScreenConfig.getConferenceId());
        List<NameValuePair> nameValuePairs = BaseSupport.buildBaseRequestMap(BaseSupport.SEND_PROGRAM, roomScreenConfig.getAuthInfo());
        nameValuePairs.add(new BasicNameValuePair("autoCheck", Boolean.TRUE.toString()));
        SouthWingProgramSendParam southWingProgramSendParam = new SouthWingProgramSendParam();
        southWingProgramSendParam.setProgramId(roomScreenConfig.getProgramId());
        southWingProgramSendParam.setName(Objects.nonNull(conference) ? conference.getName() : "会议室预约节目");
        southWingProgramSendParam.setDeviceId(roomScreenConfig.getDeviceCode());
        southWingProgramSendParam.setPriority(1);
        String[] split = bookRoom.getTimeInterval().split("-");
        LocalDateTime dateTime = DateUtils.parse(bookRoom.getEndBookTime());
        String date = DateUtils.format(dateTime, "yyyy-MM-dd");
        southWingProgramSendParam.setBeginTime(date + " " + split[0] + ":00");
        southWingProgramSendParam.setEndTime(bookRoom.getEndBookTime());

        nameValuePairs.add(new BasicNameValuePair("data", JsonUtils.serialize(Collections.singletonList(southWingProgramSendParam))));
        String result = HttpClientUtils.doPostForm(roomScreenConfig.getProgramUrl(), null, nameValuePairs);
        ResultEntity response = JsonUtils.deserialize(result, ResultEntity.class);
        if (!SUCCESS_CODE.equals(response.getErrorCode())) {
            throw new ServiceException("发送节目失败:" + response.getErrorMsg());
        }
    }

    @Override
    public boolean support(String manufacturerCode) {
        return ManufacturerCodeEnum.SOUTH_WING.name().equals(manufacturerCode);
    }

    private List<SouthWingProgramInsertParam.WebItem> buildSouthWingProgramWebItem(RoomScreenConfig screenConfig,
                                                                                   HttpServletRequest httpServletRequest
                                                                                  ) {
        List<SouthWingProgramInsertParam.WebItem> webItems = new ArrayList<>(2);
        for (int i = 0; i < 2; i++) {
            SouthWingProgramInsertParam.WebItem webItem = new SouthWingProgramInsertParam.WebItem();
            webItem.setId(UUID.randomUUID().toString());
            webItem.setUrl(
                    httpServletRequest.getHeader(REQUEST_HEADER_ORIGIN) + String.format(ROOM_SCREEN_URL, screenConfig.getConferenceId()));
            webItem.setLen("10");
            webItems.add(webItem);
        }
        return webItems;
    }
}
