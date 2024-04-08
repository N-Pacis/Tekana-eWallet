package rw.pacis.tekanaewallet.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import rw.pacis.tekanaewallet.security.dtos.CustomUserDTO;

import java.util.UUID;

public interface IJwtService {

    String extractUserName(String token);

    String extractId(String token);

    UUID extractSessionId(String token);

    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);

    CustomUserDTO extractLoggedInUser();
}
