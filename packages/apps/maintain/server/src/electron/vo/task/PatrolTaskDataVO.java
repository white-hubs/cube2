package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatrolTaskDataVO {
    /**
     * 巡更记录id
     */
    private Long id;
    /**
     * 巡更点名称
     */
    private Long locationId;
    private String locationName;
    private String taskName;
    /**
     * nfc芯片编号
     */
    private String nfcCode;
    /**
     * 蓝牙编号
     */
    private String bluetoothCode;
    /**
     * 所属区域
     */
    private List<AreaDTO> arae;

    /**
     * 巡更人员
     */
    private String userId;
    private String userName;

    /**
     * 巡更时间
     */
    private LocalDateTime patrolDate;
    /**
     * 任务开始时间
     */
    private LocalDateTime startDate;
    /**
     * 任务结束时间
     */
    private LocalDateTime endDate;
    /**
     * 巡更结果
     */
    private String patrolResult;
    /**
     * 隐患单id
     */
    private String hiddenId;
    /**
     * 说明
     */
    private String remark;
    /**
     * 序号
     */
    private Long orderNum;
    /**
     * 经度
     */
    private String nfcLongitude;
    /**
     * 纬度
     */
    private String nfcDimension;
    /**
     * 巡更方式
     */
    private String patrolWay;
    /**
     * 文件
     */
    private List<FileVO> files;
    /**
     * 巡更点负责人id
     */
    private Long superintendentUserId;
    /**
     * 巡更点负责人id
     */
    private String superintendentUserName;
    /**
     * 该点位是否是当前登陆人的
     */
    private Boolean superintendent;
}
