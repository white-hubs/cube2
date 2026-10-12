package com.guhecloud.kaihe.scada.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.kaihe.scada.server.dto.FigureKindDTO;
import com.guhecloud.kaihe.scada.server.entity.FigureKind;

public interface FigureKindService extends IService<FigureKind> {

	void save(FigureKindDTO figureKindDTO);

	void update(FigureKindDTO figureKindDTO);

	void remove(Integer id, String folder, Integer projectId);

}
