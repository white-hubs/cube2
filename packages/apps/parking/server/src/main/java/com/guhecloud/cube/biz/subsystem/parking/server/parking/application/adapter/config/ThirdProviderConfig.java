package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.config;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.ParkingThirdProviderFactory;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingProviderEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.provider.ParkingProviderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ThirdProviderConfig {

    @Bean
    public ParkingThirdProviderFactory thirdProviderFactory(Map<ParkingProviderEnum, ParkingProviderGateway> providerMap) {
        return new ParkingThirdProviderFactory(providerMap);
    }

    @Bean
    public Map<ParkingProviderEnum, ParkingProviderGateway> parkingProviderGateway(List<ParkingProviderGateway> providers) {
        return providers.stream()
                .collect(Collectors.toMap(ParkingProviderGateway::getProvider, p -> p));
    }
}
