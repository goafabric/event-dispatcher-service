package org.goafabric.eventdispatcher;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.security.AccessController;


/**
 * Created by amautsch on 26.06.2015.
 */

@SpringBootApplication
@RegisterReflectionForBinding({AccessController.class, javax.security.auth.Subject.class})
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner init(ApplicationContext context) {
        return args -> {if ((args.length > 0) && ("-check-integrity".equals(args[0]))) {SpringApplication.exit(context, () -> 0);}};
    }

}
