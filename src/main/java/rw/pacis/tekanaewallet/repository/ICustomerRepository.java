package rw.pacis.tekanaewallet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rw.pacis.tekanaewallet.model.Customer;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.enums.EGender;
import rw.pacis.tekanaewallet.model.enums.ERole;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE (:status IS NULL OR c.status =:status) AND (LOWER(CONCAT(TRIM(c.firstName), ' ', TRIM(c.lastName))) LIKE LOWER(CONCAT('%', :query, '%'))) OR LOWER(TRIM(c.email)) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(TRIM(c.phoneNumber)) LIKE LOWER(CONCAT('%', :query, '%')) ")
    Page<Customer> searchAll(String query, EUserStatus status, Pageable pageable);

}
