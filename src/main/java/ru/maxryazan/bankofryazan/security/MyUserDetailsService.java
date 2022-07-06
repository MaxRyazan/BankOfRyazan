package ru.maxryazan.bankofryazan.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.service.ClientService;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final ClientService clientService;
    private final BCryptPasswordEncoder passwordEncoder;

    public MyUserDetailsService(ClientService clientService, BCryptPasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Client client = clientService.findByPhoneNumber(phoneNumber);

        return User.builder()
                .username(client.getPhoneNumber())
                .password(client.getPinCode())
                .roles()
                .build();
    }

}
