package com.cavin.culture.webFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {


    /**
            * 在配置文件中配置的文件保存路径
     */
    @Value("${file.location}")
    private String location;
    @Value("${file.serverurl}")
    private String serverurl;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大KB,MB
        factory.setMaxFileSize("10MB");
        //设置总上传数据总大小
        factory.setMaxRequestSize("100MB");
        return factory.createMultipartConfig();
    }

    /**
     * @return the serverurl
     */
    public String getServerurl() {
        return serverurl;
    }

    /**
     * @param serverurl the serverurl to set
     */
    public void setServerurl(String serverurl) {
        this.serverurl = serverurl;
    }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/image");
            //windows本地文件目录
//            registry.addResourceHandler("/restaurantRes/**").addResourceLocations("file:D:/restaurantRes/");
        }



}
