package com.guhecloud.cube.biz.ouaa.server.permission.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminalNode {
    private Long id;
    private String name;
    private List<ApplicationNode> apps;
}