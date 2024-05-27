package ru.startup.verifier_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.startup.verifier_service.model.User;
import ru.startup.verifier_service.repository.UserRepository;
import ru.startup.verifier_service.security.exception.RegistrationException;
import ru.startup.verifier_service.security.service.impl.JwtUserDetailsService;
import ru.startup.verifier_service.security.model.JwtResponse;
import ru.startup.verifier_service.security.model.Role;
import ru.startup.verifier_service.security.model.dto.ChangeUserRightsDto;
import ru.startup.verifier_service.security.model.dto.LoginDto;
import ru.startup.verifier_service.security.model.dto.RegistrationDto;
import ru.startup.verifier_service.security.service.JwtTokenService;
import ru.startup.verifier_service.service.UserService;
import ru.startup.verifier_service.util.converter.Converter;

import java.util.*;

/**
 * Implementation of the UserManagementService interface providing methods for managing user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    /**
     * This DAO is responsible for data access operations related to users.
     */
    private final UserRepository userRepository;

    /**
     * The service responsible for the token
     */
    private final JwtTokenService jwtTokenService;

    /**
     * The service responsible for managing user details for JWT authentication.
     */
    private final JwtUserDetailsService jwtUserDetailsService;

    /**
     * The password encoder used for encoding and decoding passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The converter used for converting one type of object to another.
     */
    private final Converter converter;

    @Override
    @Transactional
    public User registerUser(RegistrationDto registrationDto) {
        userRepository.findByEmail(registrationDto.getEmail())
                .ifPresent(u -> {
                    throw new RegistrationException("Такой пользователь уже существует!");
                });
        User user = generateNewUser(registrationDto);
        userRepository.save(user);
        return user;
    }

    @Override
    public JwtResponse login(String email) {
        String accessToken = jwtTokenService.createAccessToken(email);
        String refreshToken = jwtTokenService.createRefreshToken(email);

        return new JwtResponse(email, accessToken, refreshToken);
    }

    @Override
    public boolean verifierPassword(LoginDto loginDto) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginDto.getEmail());
        return passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword());
    }

    @Override
    @Transactional
    public User changeUserPermissions(String uuidStr, ChangeUserRightsDto changeUserRightsDto) {
        Role role = changeUserRightsDto.newRole();
        UUID uuid = converter.convert(uuidStr, UUID::fromString, "Invalid UUID");
        userRepository.updateUserRole(uuid, role.toString());
        return userRepository.findByUUID(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с uuid = "
                                                                 + uuidStr + " не существует!"));
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public JwtResponse updateToken(String refreshToken) {
        return jwtTokenService.refreshUserToken(refreshToken);
    }

    private User generateNewUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setFullName(registrationDto.getFullName());
        user.setCompany(registrationDto.getCompany());
        user.setInn(registrationDto.getInn());
        user.setOgrn(registrationDto.getOgrn());
        user.setPosition(registrationDto.getPosition());
        user.setRole(Role.USER.toString());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        return user;
    }
}
