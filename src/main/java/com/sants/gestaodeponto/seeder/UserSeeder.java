package com.sants.gestaodeponto.seeder;

import com.sants.gestaodeponto.domain.user.User;
import com.sants.gestaodeponto.domain.user.UserRole;
import com.sants.gestaodeponto.domain.user.WorkSchedule;
import com.sants.gestaodeponto.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class UserSeeder implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserSeeder.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Verifica se o argumento "seeder" foi passado
        if (args.getOptionValues("seeder") != null) {
            List<String> seeder = args.getOptionValues("seeder");
            if (seeder.contains("user")) {
                seedAdminUser();
                log.info("Success run user seeder");
            }
        } else {
            log.info("User seeder skipped");
        }
    }

    private void seedAdminUser() {
        if (userRepository.findByEmail("admin@gmail.com") == null) {
            // Cria o usuário admin
            User admin = new User(
                    "Administrador",
                    "admin@gmail.com",
                    new BCryptPasswordEncoder().encode("admin"),
                    UserRole.ADMIN,
                    WorkSchedule.EIGHT_HOURS
            );
            userRepository.save(admin);
            log.info("Usuário administrador criado com sucesso.");
        } else {
            log.info("Usuário administrador já existe.");
        }
    }
}
