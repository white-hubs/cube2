package com.guhecloud.cube.biz.ouaa.dcm.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface OuaaDcmService {

    List<RequestedConfigItem> fetchRuntimeAppGroupConfig();

    List<RequestedConfigItem> applyAppGroupConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeAppConfig();

    List<RequestedConfigItem> applyAppConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeProductConfig();

    List<RequestedConfigItem> applyProductConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeTerminalConfig();

    List<RequestedConfigItem> applyTerminalConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeCardConfig();

    List<RequestedConfigItem> applyCardConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeProdAppConfig();

    List<RequestedConfigItem> applyProdAppConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeMenuConfig();

    List<RequestedConfigItem> applyMenuConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeRoleConfig(Long tenant);

    List<RequestedConfigItem> applyRoleConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimeRoleTerminalConfig(Long tenant);

    List<RequestedConfigItem> applyRoleTerminalConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimeRoleAppConfig(Long tenant);

    List<RequestedConfigItem> applyRoleAppConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimeRoleMenuConfig(Long tenant);

    List<RequestedConfigItem> applyRoleMenuConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimePositionConfig(Long tenant);

    List<RequestedConfigItem> applyPositionConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimeTerminalAppConfig();

    List<RequestedConfigItem> applyTerminalAppConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeTenantProdConfig(Long tenant);

    List<RequestedConfigItem> applyTenantProdConfig(List<RequestedConfigItem> diffs, Long tenant);

    List<RequestedConfigItem> fetchRuntimeTenantAppConfig(Long tenant);

    List<RequestedConfigItem> applyTenantAppConfig(List<RequestedConfigItem> diffs, Long tenant);

    List<RequestedConfigItem> fetchRuntimePermission();

    List<RequestedConfigItem> applyPermissionConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeTenantSubjectPermission(Long tenant);

    List<RequestedConfigItem> applyTenantSubjectPermissionConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);
}
