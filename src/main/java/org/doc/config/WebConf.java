package org.doc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConf implements WebMvcConfigurer {

//    @Value("${Config.file_path}")
//    private String configFilePath;
//
//    @Value("${Config.uav_path}")
//    private String uavPath;

    @Resource
    private CustomInterceptor interceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String configFilePath = "/pdf";
//        registry.addResourceHandler(new String[]{"/**"}).addResourceLocations(new String[]{"classpath:/static/"});
        String location = "file:" + configFilePath + "/";
        registry.addResourceHandler(configFilePath+"/**").addResourceLocations(location).resourceChain(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(interceptor).addPathPatterns(uavPath+"/**");
    }
}
