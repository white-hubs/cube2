package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class DiagramUpdateDTO {

	private String action;
	private String diagram;
	private String png;
	private String linkMap;
	private String svg;
	private Integer diagramId;
	private Integer projectId;

}
