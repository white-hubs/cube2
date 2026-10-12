package com.guhecloud.kaihe.scada.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.kaihe.scada.server.entity.Figure;
import org.springframework.web.multipart.MultipartFile;

public interface FigureService extends IService<Figure> {

	int save(String fileId, Integer figureKindId, String folder, MultipartFile fileData, Integer projectId);
	
    void rename(Integer id, String name);
    
    void remove(Integer figureId, String fileName);

}
