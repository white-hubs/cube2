package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 机组
 */
@Data
public class MachinesVO {
    Long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 参数
     */
    private String arguments;
    /**
     * 附件（文件ids）
     */
    private String fileIds;
    /**
     * 关联的手术室ids
     */
    private String roomIds;
    private LocalDateTime createTime;
    private Integer tenant;
    private List<FileVO> fileList;
    /**
     * 关联手术室数量
     */
    private Integer roomNumber;

    public int getRoomNumber() {
        if (this.roomNumber != null) {
            return this.roomNumber;
        }
        if (StringUtils.isNotBlank(this.roomIds)) {
            return this.roomIds.split(",").length;
        }
        return 0;
    }
}
