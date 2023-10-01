package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.configuration.security.JwtService;

import org.example.dto.auth.AuthResponseDTO;
import org.example.dto.auth.LoginDTO;
import org.example.dto.auth.RegisterDTO;
import org.example.entities.UserEntity;
import org.example.mappers.AccountMapper;
import org.example.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccountMapper accountMapper;

    public AuthResponseDTO login(LoginDTO request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        //var password = passwordEncoder.
        var isValid = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!isValid) {
            throw new UsernameNotFoundException("User not found");
        }

        var jwtToken = jwtService.generateAccessToken(user);
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
    public void register(RegisterDTO request) {
        var user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new UsernameNotFoundException("User email already registered");
        }
        else {
            UserEntity newUser = accountMapper.itemDtoToUser(request);
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(newUser);
        }
    }
}