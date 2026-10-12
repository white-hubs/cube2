package com.guhecloud.cube.iot.mgt.core.data.msg.device;

/**
 * Represents an internal event indicating a device entity and its data should be permanently removed.
 * This is an irreversible action.
 *
 * @param gatewayId The unique identifier of the gateway that owns the device.
 * @param deviceId The unique identifier of the device to be removed.
 * @param tenantId The unique identifier of the tenant.
 * @param remover  Identifier for the user or service that initiated the removal.
 * @param reason   An optional reason for the removal.
 */
public record DeviceRemove(String gatewayId, String deviceId, Long tenantId, String remover, String reason) {

}
