package rw.pacis.tekanaewallet.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.UserAccountAudit;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterUserDTO;
import rw.pacis.tekanaewallet.model.dtos.request.SetPasswordDTO;
import rw.pacis.tekanaewallet.model.enums.ERole;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    UserAccount getLoggedInUser() throws ResourceNotFoundException;

    UserAccount create(RegisterUserDTO dto) throws BadRequestException;

    UserAccount getById(UUID id) throws ResourceNotFoundException;

    UserAccount activate(UUID userId) throws ResourceNotFoundException, BadRequestException;

    UserAccount deactivate(UUID userId) throws ResourceNotFoundException, BadRequestException;

    Page<UserAccount> searchAll(String q, ERole role, EUserStatus status, Pageable pageable) throws ResourceNotFoundException;

    @Transactional
    UserAccount resetPassword(UUID id, SetPasswordDTO passwordDTO) throws ResourceNotFoundException, BadRequestException;

    List<UserAccountAudit> getAuditByUser(UUID id) throws ResourceNotFoundException;
}
