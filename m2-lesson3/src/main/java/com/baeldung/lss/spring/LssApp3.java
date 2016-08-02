package com.baeldung.lss.spring;

import com.baeldung.lss.model.User;
import com.baeldung.lss.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static java.lang.Long.valueOf;
import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@ComponentScan("com.baeldung.lss")
@EnableJpaRepositories("com.baeldung.lss")
@EntityScan("com.baeldung.lss.model")
public class LssApp3 {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public Converter<String, User> messageConverter() {
        return new Converter<String, User>() {
            @Override
            public User convert(String id) {
                return userRepository.findOne(valueOf(id));
            }
        };
    }

    public static void main(String[] args) throws Exception {
        run(new Class[]{LssApp3.class, LssSecurityConfig.class, LssWebMvcConfiguration.class, EmailConfig.class}, args);
    }

}
