package com.guhecloud.cube.biz.dispatch.server.vo.extend;

import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupVO;
import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 调度设备-扩展
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchDeviceExtendVO extends DispatchDeviceVO {
    private Boolean isRecord;
    private Boolean isVideo;
    private Integer deviceLevel;
    private String remark;
    private List<DispatchGroupVO> groups;
}