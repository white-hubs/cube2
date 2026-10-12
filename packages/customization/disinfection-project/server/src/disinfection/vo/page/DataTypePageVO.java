package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

@Data
public class DataTypePageVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String dataName;
    private String remark;
    private FileVO iconFile;
}
