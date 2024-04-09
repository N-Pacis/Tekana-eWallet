package rw.pacis.tekanaewallet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.UserAccountLoginHistory;

import java.util.List;
import java.util.UUID;

@Repository
public interface IUserAccountLoginHistoryRepository extends JpaRepository<UserAccountLoginHistory, UUID> {

    Integer countAllByUserAndUserAgent(UserAccount user, String userAgent);

    Page<UserAccountLoginHistory> findByUser(UserAccount user, Pageable pageable);
}
