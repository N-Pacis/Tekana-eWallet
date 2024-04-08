package rw.pacis.tekanaewallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.pacis.tekanaewallet.model.Customer;
import rw.pacis.tekanaewallet.model.CustomerWallet;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICustomerWalletRepository extends JpaRepository<CustomerWallet, String> {

    List<CustomerWallet> findByCustomer(Customer customer);
}
