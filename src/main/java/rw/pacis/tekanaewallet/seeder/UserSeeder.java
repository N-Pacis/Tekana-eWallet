package rw.pacis.tekanaewallet.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterUserDTO;
import rw.pacis.tekanaewallet.model.enums.ERole;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;
import rw.pacis.tekanaewallet.repository.IUserRepository;

@Configuration
public class UserSeeder {
    @Bean
    CommandLineRunner commandLineRunner(
                                        IUserRepository userRepo
                                        ) {
        return args -> {

            if(userRepo.findByEmail("admin@tekana.rw").isEmpty()){

                RegisterUserDTO userDto = new RegisterUserDTO("System", "Admin","admin@tekana.rw","");

                UserAccount userAccount = new UserAccount(userDto);
                userAccount.setRole(ERole.ADMIN);
                userAccount.setStatus(EUserStatus.ACTIVE);
                //Password is Qwerty@570 - A better approach would be sealing the secret using kubeseal but due to infrastructure limit this is what I came with.
                userAccount.setPassword("$2a$12$N9hr.Cw4ySeAxcVdTlmzF.nAFq41zST5YJRUhDs/N0Qcc4nxdGwUu");

                userRepo.save(userAccount);
            }

        };
    }
}