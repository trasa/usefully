package com.meancat.usefully.redis;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { RedisConfiguration.class })
public class RedisConfiguration {
}
