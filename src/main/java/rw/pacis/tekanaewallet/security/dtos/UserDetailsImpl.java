package rw.pacis.tekanaewallet.security.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.enums.ERole;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;
@Slf4j
public class UserDetailsImpl implements UserDetails {

    private UUID id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String fullName;

    @NotNull
    private String email;

    @NotNull
    private ERole role;

    @NotNull
    private EUserStatus status;

    @JsonIgnore
    private String password;

    private Collection<GrantedAuthority> authorities;

    public UserDetailsImpl(UUID id, String email, String password,
                           ERole role, String firstName, String lastName,
                           EUserStatus status, Collection<GrantedAuthority> authorities ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullName = firstName+ " "+lastName;
        this.firstName = firstName;
        this.lastName =  lastName;
        this.status = status;
        this.authorities = authorities;

    }
    public static UserDetailsImpl build(UserAccount user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus(),
                user.getAuthorities()
        );
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserAccount getUserAccount() {
        return UserAccount.builder()
                .id(id)
                .email(email)
                .password(password)
                .role(role)
                .firstName(firstName)
                .lastName(lastName)
                .status(status)
                .build();
    }
}
