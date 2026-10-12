package com.guhecloud.kaihe.scada.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.kaihe.scada.server.dto.DiagramDataDTO;
import com.guhecloud.kaihe.scada.server.entity.DiagramData;
import com.guhecloud.kaihe.scada.server.mapper.DiagramDataMapper;
import com.guhecloud.kaihe.scada.server.service.DiagramDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiagramDataServiceImpl extends ServiceImpl<DiagramDataMapper, DiagramData> implements DiagramDataService {

	private final DiagramDataMapper diagramDataMapper;

	@Override
	public void save(DiagramDataDTO diagramDataDTO) {
		diagramDataMapper.insert(diagramDataDTO.convert());
	}

}
