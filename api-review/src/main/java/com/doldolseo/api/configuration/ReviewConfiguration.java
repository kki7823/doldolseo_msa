package com.doldolseo.api.configuration;

import com.doldolseo.api.util.UploadReviewFileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/*
 * Configuration 클래스
 *
 * @Author 김경일
 * @Date 2021/08/07
 */

@Configuration
public class ReviewConfiguration {

    //Entity-DTO 간 변환
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UploadReviewFileUtil uploadReviewFileUtil(){
        return new UploadReviewFileUtil(uploadPath());
    }

    //이미지 저장될 절대 경로(로컬)
    @Bean(name = "uploadPath")
    public String uploadPath() {
        return System.getProperty("user.dir") + "/src/main/resources/static/";
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        return new MappingJackson2HttpMessageConverter();
    }

    //HTTP hidden Method : delete, put, patch ..
    @Bean
    public HiddenHttpMethodFilter httpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}
