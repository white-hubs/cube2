package com.guhecloud.cube.iot.north.bridge.consumer.cfg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RabbitSubscribeConfig extends SubscribeConfig {

    /**
     * RabbitMQ host. Ignored if an address is set.
     */
    private String host = "localhost";

    /**
     * RabbitMQ port. Ignored if an address is set. Default to 5672, or 5671 if SSL is
     * enabled.
     */
    private Integer port;

    /**
     * Login user to authenticate to the broker.
     */
    private String username = "guest";

    /**
     * Login to authenticate against the broker.
     */
    private String password = "guest";

    /**
     * Virtual host to use when connecting to the broker.
     */
    private String virtualHost;

    /**
     * consumer group name,by default, it uses spring application name
     */
    private String consumerGroup;
}
