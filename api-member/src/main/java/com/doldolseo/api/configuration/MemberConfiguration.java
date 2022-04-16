package com.doldolseo.api.configuration;

import com.doldolseo.api.utils.UploadProfileUtil;
import com.doldolseo.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class MemberConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name = "uploadPath")
    public String uploadPath() {
        return System.getProperty("user.dir") + "/src/main/resources/static/images";
    }

    @Bean
    public UploadProfileUtil uploadFileUtil() {
        return new UploadProfileUtil(uploadPath());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
