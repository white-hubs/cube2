package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.guhecloud.kaihe.scada.server.entity.DiagramData;
import com.guhecloud.kaihe.scada.server.infrastructure.dto.ConvertionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class DiagramDataDTO extends ConvertionDTO<DiagramDataDTO, DiagramData> {

	public DiagramDataDTO() {
	}

	public DiagramDataDTO(DiagramData diagramData) {
		super();
		map(diagramData);
	}

	public DiagramDataDTO(List<DiagramData> list) {
		super();
		map(list);
	}

	private Integer DiagramID;

	private String TYPE;

	private String FileName;

	private Integer FileSize;

	private Date LastUpdate;
	private Integer projectId;

}
