package me.khabib.chat.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("me.khabib.chat")
public class AppConfig {
}
