package com.app.auth.service.impl;

import com.app.auth.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
import java.util.List;
//
@Service
public class AdminServiceImpl implements AdminService {
//
//    private final com.app.auth.service.impl.UserRepository userRepository;
//
//    @Autowired
//    public AdminServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    @Override
//    public List<User> getAllEnableUsers() {
//        return userRepository.findAllByIsEnabledTrue();
//    }
//
//    @Override
//    public String disableUserByEmail(String email) {
//        User userToDisable = getUserByEmail(email);
//        userToDisable.setEnabled(false);
//        userRepository.save(userToDisable);
//        return "Disabled successfully the user with the email: " + email;
//    }
//
//    @Override
//    public ResponseEntity<String> changeRoleToAnUser(ChangeRoleRequest request) {
//        User user = getUserByEmail(request.getEmail());
//        user.setRole(UserRole.toRole(request.getNewRole()));
//
//        userRepository.save(user);
//        return ResponseEntity.ok(user.getUsername() + "'s role updated successfully to an " + request.getNewRole());
//    }
//
//    private User getUserByEmail(String email) {
//        return userRepository.findByUsername(email)
//                .orElseThrow(() -> new UserNotFoundException("User not found with the email: " + email));
//    }
//
//
}
