package com.meancat.usefully.jersey;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

import static com.codahale.metrics.MetricRegistry.name;

public class MetricFilter implements ContainerResponseFilter, ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(MetricFilter.class);
    private Meter incoming, outgoing;

    public MetricFilter(MetricRegistry metricRegistry) {
        incoming = metricRegistry.meter(name(MetricFilter.class, "requests"));
        outgoing = metricRegistry.meter(name(MetricFilter.class, "requests"));
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        logger.debug("request");
        incoming.mark();
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        logger.debug("response");
        outgoing.mark();
    }
}
