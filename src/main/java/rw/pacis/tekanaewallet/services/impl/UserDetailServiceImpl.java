package rw.pacis.tekanaewallet.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.repository.IUserRepository;
import rw.pacis.tekanaewallet.security.dtos.UserDetailsImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Optional<UserAccount> user = userRepository.findByEmail(username);

        if(user.isPresent())   {
            UserAccount userAccount =  user.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(userAccount.getRole().toString()));
            userAccount.setAuthorities(authorities);

            return  UserDetailsImpl.build(userAccount);

        } else{
            throw new UsernameNotFoundException("exceptions.notFound.user");
        }
    }

}