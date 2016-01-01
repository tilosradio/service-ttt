package hu.tilos.radio.backend.ttt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

@Component
public class PortCustomizer implements EmbeddedServletContainerCustomizer {

    @Value("${port.default}")
    int defaultPort;

    @Value("${port.ttt.offset}")
    int offset;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        System.out.println(defaultPort);
        container.setPort(defaultPort + offset);
    }

}