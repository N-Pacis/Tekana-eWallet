package rw.pacis.tekanaewallet.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.InvalidCredentialsException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.UserAccountAudit;
import rw.pacis.tekanaewallet.model.UserAccountLoginHistory;
import rw.pacis.tekanaewallet.model.dtos.request.UpdatePasswordDTO;
import rw.pacis.tekanaewallet.model.enums.EAuditType;
import rw.pacis.tekanaewallet.model.enums.ELoginStatus;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;
import rw.pacis.tekanaewallet.repository.IUserAccountAuditRepository;
import rw.pacis.tekanaewallet.repository.IUserAccountLoginHistoryRepository;
import rw.pacis.tekanaewallet.repository.IUserRepository;
import rw.pacis.tekanaewallet.security.dtos.*;
import rw.pacis.tekanaewallet.security.service.IJwtService;
import rw.pacis.tekanaewallet.services.IAuthenticationService;
import rw.pacis.tekanaewallet.services.IUserService;
import rw.pacis.tekanaewallet.utils.Constants;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IUserRepository userRepository;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final IUserService userService;

    private final PasswordEncoder passwordEncoder;

    private final IUserAccountLoginHistoryRepository userAccountLoginHistoryRepository;

    private final IUserAccountAuditRepository userAccountAuditRepository;

    @Override
    public LoginResponseDTO signIn(LoginRequest request, String userAgent, String deviceType) {

        UserAccount user = null;
        request.setLogin(request.getLogin().trim());
        request.setPassword(request.getPassword().trim());

        try{

            user = userRepository.findByEmail(request.getLogin()).orElseThrow(InvalidCredentialsException::new);

            if(user.getStatus().equals(EUserStatus.INACTIVE) || user.getStatus().equals(EUserStatus.PENDING) )
                throw new InvalidCredentialsException("exceptions.badRequest.accountLocked");

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

            saveLoginHistory(user, userAgent, deviceType);

            user.setLastLogin(LocalDateTime.now(ZoneId.of("Africa/Kigali")));
            userRepository.save(user);

            var jwt = generateJWTToken(user);

            return LoginResponseDTO.builder().token(jwt).build();

        }catch(Exception e){
            log.info("Exception: " + e.getMessage());
            throw new InvalidCredentialsException("exceptions.invalidEmailPassword");
        }
    }

    @Override
    public void signOut() throws ResourceNotFoundException {
        UserAccount userAccount = userService.getLoggedInUser();

        invalidateUserLogin(userAccount);
    }

    @Override
    public void invalidateUserLogin(UserAccount userAccount){
        userAccount.setSessionId(null);
        userAccount.setLoginStatus(ELoginStatus.INACTIVE);

        userRepository.save(userAccount);
    }

    public void saveLoginHistory(UserAccount userAccount, String userAgent, String deviceType){
        UserAccountLoginHistory userAccountLoginHistory = new UserAccountLoginHistory(userAgent, deviceType, userAccount);;
        userAccountLoginHistoryRepository.save(userAccountLoginHistory);
    }

    @Transactional
    @Override
    public UserAccount updatePassword(UpdatePasswordDTO dto) throws ResourceNotFoundException, BadRequestException {
        UserAccount userAccount = userService.getLoggedInUser();

        if(!passwordEncoder.matches(dto.getOldPassword(), userAccount.getPassword())) {
            throw new BadRequestException("exceptions.badRequest.passwordMissMatch");
        }

        userAccount.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.userRepository.save(userAccount);

        CustomUserDTO userDTO = this.jwtService.extractLoggedInUser();
        UserAccountAudit audit = new UserAccountAudit(userAccount, EAuditType.UPDATE, userDTO.getId(), userDTO.getFullNames(), userDTO.getEmailAddress(), "Password updated");
        this.userAccountAuditRepository.save(audit);

        return userAccount;
    }


    private JwtAuthenticationResponse generateJWTToken(UserAccount user){
        List<GrantedAuthority> privileges = new ArrayList<>();
        privileges.add(new SimpleGrantedAuthority(user.getRole().toString()));

        user.setAuthorities(privileges);

        return JwtAuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(UserDetailsImpl.build(user))).tokenType(Constants.TOKEN_TYPE).build();
    }
}
