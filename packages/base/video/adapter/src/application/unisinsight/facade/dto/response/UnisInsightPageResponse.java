package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnisInsightPageResponse<T> {

    private List<T> data;
}
