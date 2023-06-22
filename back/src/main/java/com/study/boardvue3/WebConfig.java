package com.study.boardvue3;

import com.study.boardvue3.encoder.Encryptor;
import com.study.boardvue3.encoder.SHA256Encryptor;
import com.study.boardvue3.filter.LoggingURIFilter;
import com.study.boardvue3.validator.BoardValidator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * http://localhost:5173의 접속을 허용한다.
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("http://localhost:5173")
                .allowedHeaders("*")
                .exposedHeaders("content-disposition")
                .maxAge(3600);
    }

    /**
     * URIFilter 등록
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<LoggingURIFilter> URIFilter() {
        return new FilterRegistrationBean<LoggingURIFilter>(new LoggingURIFilter());
    }

    /**
     * SHA256Encryptor Bean으로 등록
     *
     * @return
     */
    @Bean
    public Encryptor sha256Encryptor() {
        return new SHA256Encryptor();
    }

    @Bean
    public BoardValidator boardValidator() {
        return new BoardValidator();
    }
}
