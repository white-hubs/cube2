package com.guhecloud.kaihe.scada.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "GHD_DIAGRAMDATA")
public class DiagramData {

	@TableId(value = "DiagramID")
	private Integer DiagramID;

	@TableField("TYPE")
	private String TYPE;

	@TableField("FileName")
	private String FileName;

	@TableField("FileSize")
	private Integer FileSize;

	@TableField("LastUpdate")
	private Date LastUpdate;

	@TableField("projectId")
	private Integer projectId;

}
