package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class DiagramLoadDTO {

	private String AppKey;
	private String MenuId;
	private String action;
	private String diagramId;

}
