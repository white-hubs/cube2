package com.guhecloud.kaihe.scada.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "GHD_DIAGRAM")
public class Diagram {

	@TableId(value = "ID", type = IdType.AUTO)
	private Integer ID;

	@TableField("Title")
	private String Title;

	@TableField("Description")
	private String Description;

	@TableField("Public")
	private Boolean Public;

	@TableField("CreatedDate")
	private Date CreatedDate;

	@TableField("LastUpdate")
	private Date LastUpdate;

	@TableField("Size")
	private Integer Size;

	@TableField("Category")
	private Integer Category;

	@TableField("projectId")
	private Integer projectId;

}
