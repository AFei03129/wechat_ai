package xlike.top.kn_ai_chat.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.time.Duration;

/**
 * @author xlike
 */
@Configuration
public class WeChatConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder
                .connectTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60))
                .build();

        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        // ASR 网关不兼容未知长度的流式 multipart；只缓冲此接口以发送 Content-Length。
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()) {
            @Override
            protected boolean shouldBuffer(URI uri, HttpMethod method) {
                return HttpMethod.POST.equals(method) && uri.getPath().endsWith("/audio/transcriptions");
            }
        });

        return restTemplate;
    }
}
