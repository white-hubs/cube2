package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

@Data
public class ItemDevicePageVO {

    private Long id;
    private Long deviceId;
    private String deviceCode;
    private String deviceName;
    /**
     * 机组id
     */
    private Long machinesId;
    /**
     * 机组编号
     */
    private String machinesCode;

    private Long deptId;
    private String deptName;
    private String arguments;
    private String fileIds;
    private List<FileVO> fileList;
    List<SurgeryItemVO> itemList;
}
