package com.guhecloud.kaihe.scada.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.kaihe.scada.server.dto.DiagramDataDTO;
import com.guhecloud.kaihe.scada.server.entity.DiagramData;

public interface DiagramDataService extends IService<DiagramData> {
	
	public static final String TYPE_DMO = "dia";
	public static final String TYPE_SVG = "svg";
	public static final String TYPE_JPG = "jpg";
	public static final String TYPE_PNG = "png";
	public static final String TYPE_CSV = "csv";
	
	void save(DiagramDataDTO diagramDataDTO);

}
