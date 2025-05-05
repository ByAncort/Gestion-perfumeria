package com.app.jwtauth.Service;


import com.app.jwtauth.Models.Role;
import com.app.jwtauth.Models.User;
import com.app.jwtauth.Repository.PermissionRepository;
import com.app.jwtauth.Repository.RoleRepository;
import com.app.jwtauth.Repository.UserRepository;
import com.app.jwtauth.config.jwt.JwtUtils;
import com.app.jwtauth.dto.PermissionDto;
import com.app.jwtauth.dto.RoleDto;
import com.app.jwtauth.dto.ServiceResult;
import com.app.jwtauth.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;


    @Autowired
    private JwtUtils jwtUtils;
    private Mapper mapper;

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .usermane(user.getUsername())
                .Email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> RoleDto.builder()
                                .name(role.getName())
                                .permissions(role.getPermissions().stream()
                                        .map(perm -> PermissionDto.builder()
                                                .name(perm.getName())
                                                .build())
                                        .collect(Collectors.toSet()))
                                .build())
                        .collect(Collectors.toList()))
                .enabled(user.isEnabled())
                .build();
    }

    public UserDto getLoggedInUser() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();

                return userRepository.findByUsername(username)
                        .map(this::convertToDto)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            } else if (principal instanceof String) {
                // Handle case where principal is just a String (username)
                return userRepository.findByUsername((String) principal)
                        .map(this::convertToDto)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + principal));
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new RuntimeException("Error: No users found.");
        }

        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ServiceResult<User> assignRoleToUser(Long userId, String roleName) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

            user.getRoles().add(role);
            User updated = userRepository.save(user);

            return new ServiceResult<>(updated);
        } catch (Exception e) {
            return new ServiceResult<>(List.of("Error assigning role: " + e.getMessage()));
        }
    }

}
