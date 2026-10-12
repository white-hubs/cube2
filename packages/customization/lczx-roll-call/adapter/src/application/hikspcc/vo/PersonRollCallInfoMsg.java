package com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.vo;

import lombok.Data;

import java.util.List;

@Data
public class PersonRollCallInfoMsg {
    /**
     * {
     * "method": "OnEventNotify",
     * "params": {
     * "ability": "rollcall_event",
     * "clients": [
     * "bff-oma"
     * ],
     * "events": [
     * {
     * "data": {
     * "text": [
     * {
     * "departmentIndexCode": "75901885767828349028",
     * "departmentName": "合肥监狱/合肥监狱/一监区",
     * "facePhoto": "http://10.19.189.208:6120/pic?3df3475efe8do-2el*81-a89o5=0137*6l8=0105*0605904=7136*=t110**psb==f42a*f74296613-8d586e-9pi108o=74204=61e",
     * "indexCode": "326100007736",
     * "name": "陈慧",
     * "orgCode": "10853744b7dc4353a84ab528ba58b0ae",
     * "recordCode": "1d1e3d2431064b8da38b2d1aa0f70cc5",
     * "remark": "会见",
     * "channelName": "一楼PTZ球机",
     * "rollcallTime": "2023-09-18T20:19:04.000+08:00",
     * "status": 1,
     * "taskType": "sporadic"
     * },
     * {
     * "departmentIndexCode": "75901885767828349028",
     * "departmentName": "合肥监狱/合肥监狱/一监区",
     * "facePhoto": "http://10.19.189.208:6120/pic?0d17=e864i24-=o5018p39e6*0o4=9140*1l0=0300675669t4=717**ps===142**fb42f66a3-7d5993a-17*le2-od8efe5743f3",
     * "indexCode": "326100006012",
     * "name": "顾萍",
     * "orgCode": "10853744b7dc4353a84ab528ba58b0ae",
     * "recordCode": "1d1e3d2431064b8da38b2d1aa0f70cc5",
     * "remark": "会见",
     * "channelName": "一楼PTZ球机",
     * "rollcallTime": "2023-09-18T20:19:04.000+08:00",
     * "status": 1,
     * "taskType": "sporadic"
     * }
     * ]
     * },
     * "eventId": "82be666a-4f29-4f6e-817f-49761807eaa4",
     * "eventType": 6140796929,
     * "happenTime": "2023-09-18T20:19:04.145+08:00",
     * "srcIndex": "prisonrollcall",
     * "srcName": "",
     * "srcType": "component",
     * "status": 0,
     * "timeout": 0
     * }
     * ],
     * "quids": [],
     * "sendTime": "2023-09-18T20:19:04.145+08:00",
     * "uids": [
     * "*"
     * ]
     * }
     * }
     */

    private String method;
    private RollCallParam params;

    @Data
    public static class RollCallParam {
        private String ability;
        private String[] quids;
        private String sendTime;
        private String[] uids;
        private String[] clients;
        private List<RollCallEvent> events;
    }

    @Data
    public static class RollCallEvent {
        private RollCallData data;
        private String eventId;
        private Long eventType;
        private String happenTime;
        private String srcIndex;
        private String srcName;
        private String srcType;
        private int status;
        private int timeout;
    }

    @Data
    public static class RollCallData {
        private List<RollCallText> text;
    }

    @Data
    public static class RollCallText {
        private String departmentIndexCode;
        private String departmentName;
        private String facePhoto;
        private String capturePhoto;
        private String indexCode;
        private String name;
        private String orgCode;
        private String recordCode;
        private String remark;
        private String rollcallTime;
        private String channelCode;
        private String channelName;
        private Float similarity;
        private int status;
        private String taskType;
        private String areaIndexCode;
        private String areaName;
    }
}
