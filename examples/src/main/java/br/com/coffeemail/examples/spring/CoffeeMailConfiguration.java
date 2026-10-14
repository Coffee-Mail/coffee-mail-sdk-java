package br.com.coffeemail.examples.spring;

import br.com.coffeemail.CoffeeMail;
import br.com.coffeemail.CoffeeMailLocale;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoffeeMailConfiguration {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

    @Bean
    public CoffeeMail coffeeMail() {
        return CoffeeMail.builder()
                .locale(CoffeeMailLocale.PT_BR)
                .timeout(REQUEST_TIMEOUT)
                .build();
    }
}
