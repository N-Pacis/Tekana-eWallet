package rw.pacis.tekanaewallet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.enums.ERole;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByEmail(String email);

    @Query("SELECT u FROM UserAccount u WHERE ((:status IS NULL OR u.status =:status) AND (LOWER(CONCAT(TRIM(u.firstName), ' ', TRIM(u.lastName))) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(TRIM(u.email)) LIKE LOWER(CONCAT('%', :query, '%')))) ")
    Page<UserAccount> searchAll(String query, EUserStatus status, Pageable pageable);

    Optional<UserAccount> findBySessionIdAndId(UUID sessionId, UUID userId);

}
