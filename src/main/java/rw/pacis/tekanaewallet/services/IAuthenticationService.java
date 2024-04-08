package rw.pacis.tekanaewallet.services;


import org.springframework.transaction.annotation.Transactional;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.dtos.request.UpdatePasswordDTO;
import rw.pacis.tekanaewallet.security.dtos.LoginRequest;
import rw.pacis.tekanaewallet.security.dtos.LoginResponseDTO;

public interface IAuthenticationService {

    LoginResponseDTO signIn(LoginRequest request, String userAgent, String deviceType) throws ResourceNotFoundException;

    void signOut() throws ResourceNotFoundException;

    void invalidateUserLogin(UserAccount userAccount);


    @Transactional
    UserAccount updatePassword(UpdatePasswordDTO dto) throws ResourceNotFoundException, BadRequestException;
}
