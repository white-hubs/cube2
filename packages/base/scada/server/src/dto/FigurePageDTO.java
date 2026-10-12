package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class FigurePageDTO {

    private Integer ID;

    private String Name;

    private String Type;

    private String folderName;

    private String FileName;

    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date LastUpdate;

}
