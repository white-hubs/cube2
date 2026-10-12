package com.guhecloud.kaihe.scada.server.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.guhecloud.kaihe.scada.server.entity.Diagram;
import com.guhecloud.kaihe.scada.server.infrastructure.dto.ConvertionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class DiagramDTO extends ConvertionDTO<DiagramDTO, Diagram> {

	public DiagramDTO() {
	}

	public DiagramDTO(Diagram diagram) {
		super();
		map(diagram);
	}

	public DiagramDTO(List<Diagram> list) {
		super();
		map(list);
	}

	private Integer ID;

	private String Title;

	private String Description;

	private Boolean Public;

	private Date CreatedDate;

	private Date LastUpdate;

	private Integer Size;

	private Integer Category;
	
	// data
	private String diagram;
	private String png;
	private String linkMap;
	private String svg;

	private Integer projectId;

}
