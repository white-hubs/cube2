package com.guhecloud.kaihe.scada.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.kaihe.scada.server.dto.DiagramDTO;
import com.guhecloud.kaihe.scada.server.dto.DiagramUpdateDTO;
import com.guhecloud.kaihe.scada.server.entity.Diagram;

public interface DiagramService extends IService<Diagram> {

	int save(DiagramDTO diagramDTO);

	void update(DiagramUpdateDTO diagramUpdateDTO);

	void remove(Integer id);

}
