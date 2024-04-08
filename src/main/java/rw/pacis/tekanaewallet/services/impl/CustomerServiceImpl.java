package rw.pacis.tekanaewallet.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.Customer;
import rw.pacis.tekanaewallet.model.CustomerWallet;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterCustomerDTO;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterCustomerWalletDTO;
import rw.pacis.tekanaewallet.model.enums.EGender;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;
import rw.pacis.tekanaewallet.repository.ICustomerRepository;
import rw.pacis.tekanaewallet.repository.ICustomerWalletRepository;
import rw.pacis.tekanaewallet.repository.IUserRepository;
import rw.pacis.tekanaewallet.services.ICustomerService;
import rw.pacis.tekanaewallet.services.IUserService;
import rw.pacis.tekanaewallet.utils.RandomUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private final IUserRepository userRepository;

    private final IUserService userService;

    private final ICustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final ICustomerWalletRepository customerWalletRepository;


    public CustomerServiceImpl(IUserRepository userRepository, IUserService userService, ICustomerRepository customerRepository, PasswordEncoder passwordEncoder, ICustomerWalletRepository customerWalletRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerWalletRepository = customerWalletRepository;
    }

    @Override
    public Customer create(RegisterCustomerDTO dto) throws BadRequestException {
        dto.setEmail(dto.getEmail().trim());
        dto.setPassword(dto.getPassword().trim());

        Optional<UserAccount> duplicateEmailAddress = this.userRepository.findByEmail(dto.getEmail());
        if (duplicateEmailAddress.isPresent())
            throw new BadRequestException("exceptions.badRequest.emailExists");

        Optional<Customer> duplicatePhoneNumber = this.customerRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (duplicatePhoneNumber.isPresent())
            throw new BadRequestException("exceptions.badRequest.phoneExists");

        Customer customer = new Customer(dto);
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        this.customerRepository.save(customer);

        return customer;

    }

    @Override
    @Transactional
    public CustomerWallet createWallet(UUID customerId, RegisterCustomerWalletDTO dto) throws ResourceNotFoundException {
        Customer customer = getById(customerId);
        String accountNumber = RandomUtil.randomNumber();

        while(customerWalletRepository.findById(accountNumber).isPresent()){
            accountNumber = RandomUtil.randomNumber();
        }

        CustomerWallet customerWallet = new CustomerWallet(accountNumber, customer,dto);

        return customerWalletRepository.save(customerWallet);
    }

    @Override
    public List<CustomerWallet> getMyWallets() throws ResourceNotFoundException {
        Customer customer = getLoggedInCustomer();

        return customerWalletRepository.findByCustomer(customer);
    }

    @Override
    public Customer getById(UUID id) throws ResourceNotFoundException {
        return customerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("exceptions.notFound.user"));
    }

    @Override
    public Customer getLoggedInCustomer() throws ResourceNotFoundException {
        UserAccount user = userService.getLoggedInUser();
        return getById(user.getId());
    }

    @Override
    public CustomerWallet getWalletById(String id) throws ResourceNotFoundException {
        return customerWalletRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("exceptions.notFound.wallet"));
    }

    @Override
    public Page<Customer> searchAll(String q, EUserStatus status, EGender gender, Pageable pageable){
        return this.customerRepository.searchAll(q, status, gender, pageable);
    }
}
