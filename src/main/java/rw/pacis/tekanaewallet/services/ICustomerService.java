package rw.pacis.tekanaewallet.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.Customer;
import rw.pacis.tekanaewallet.model.CustomerWallet;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterCustomerDTO;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterCustomerWalletDTO;
import rw.pacis.tekanaewallet.model.enums.EGender;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;

import java.util.List;
import java.util.UUID;

public interface ICustomerService {

    Customer create(RegisterCustomerDTO dto) throws BadRequestException;

    CustomerWallet createWallet(UUID customerId, RegisterCustomerWalletDTO dto) throws ResourceNotFoundException;

    List<CustomerWallet> getMyWallets() throws ResourceNotFoundException;

    Customer getById(UUID id) throws ResourceNotFoundException;

    Customer getLoggedInCustomer() throws ResourceNotFoundException;

    CustomerWallet getWalletById(String id) throws ResourceNotFoundException;

    Page<Customer> searchAll(String q, EUserStatus status, EGender gender, Pageable pageable);
}
