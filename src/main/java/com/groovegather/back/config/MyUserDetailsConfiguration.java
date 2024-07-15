package com.groovegather.back.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.groovegather.back.repositories.UserRepo;;
@Service
public class MyUserDetailsConfiguration implements UserDetailsService {

  private final UserRepo userRepo;

  public MyUserDetailsConfiguration(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userRepo.findByEmail(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("Aucun utilisateur ne correspond à cet email"));
  }

}
