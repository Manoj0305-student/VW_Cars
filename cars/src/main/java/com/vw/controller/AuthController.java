package com.vw.controller;

import com.vw.dto.AuthRequestDTO;
import com.vw.dto.ErrorResponse;
import com.vw.dto.JwtResponseDTO;
import com.vw.dto.UserInfoDTO;
import com.vw.entities.UserInfo;
import com.vw.entities.UserRole;
import com.vw.service.AuthService;
import com.vw.service.JwtService;
import com.vw.service.RolesService;
import com.vw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    AuthService authService;

    /**
     * Api for Authentication:
     * 1. Login api : POST(localhost:8080/auth/login)
     * 2. creating new user : POST(localhost:8080/auth/signup)
     * 3. creating new admin : POST(localhost:8080/auth/admin/signup)
     * 4. creating new role : POST(localhost:8080/auth/new/role)
     * 5. fetching roles : GET(localhost:8080/auth/roles)
=======
    /**
     * Api for Authentication:
     * 1. Login api : POST(localhost:8080/api/login)
     * 2. creating new user : POST(localhost:8080/api/signup)
     * 3. creating new admin : POST(localhost:8080/api/admin/signup)
     * 4. creating new role : POST(localhost:8080/api/new/role)
     * 5. fetching roles : GET(localhost:8080/api/roles)
>>>>>>> eec51da9a82706b449d9765753c462373756f7ba
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
//    @PostMapping("login")
//    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO)
//    {
////        Authentication authentication = authenticationManager.
////                authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),
////                        authRequestDTO.getPassword()));
////        if(authentication.isAuthenticated()){
////            UserInfo user = userService.fetchUserbyUsername(authRequestDTO.getUsername());
////
////             return JwtResponseDTO.builder().accessToken(jwtService.generateToken(
////                    authRequestDTO.getUsername()))
////                     .id(user.getId())
////                     .email(user.getEmail())
////                     .username(user.getUsername()).build();
////        } else {
////            throw new UsernameNotFoundException("Invalid user!");
////        }
//
//    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getUsername(),
                            authRequestDTO.getPassword()
                    )
            );
            if (authentication.isAuthenticated()) {
                UserInfo user = userService.fetchUserbyUsername(authRequestDTO.getUsername());

                JwtResponseDTO jwtResponse = JwtResponseDTO.builder()
                        .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                        .id(user.getUserId())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .userRoles(user.getRoles())
                        .build();
                return ResponseEntity.ok(jwtResponse);
            } else {
                throw new UsernameNotFoundException("Invalid user!");
            }
        }catch (BadCredentialsException | UsernameNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid User Credentials!"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("signup")
    public UserInfo createUser(@RequestBody UserInfoDTO userInfo){

        return userService.saveUsers(userInfo);
    }

    @PostMapping("admin/signup")
    public UserInfo createAdmin(@RequestBody UserInfoDTO userInfo){

        return userService.saveAdmins(userInfo);
    }


    @PostMapping("new/role")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRole createRole(@RequestBody UserRole userRole){

        return rolesService.saveRoles(userRole);
    }


    @GetMapping("roles")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserRole>> showRoles(){
        return rolesService.showRoles();
    }

    @GetMapping("users")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserInfo>> showUsers(){
        return userService.fetchUser();
    }

    @GetMapping("user/{id}")
    public UserInfo getUser(@PathVariable long id){
        return userService.fetchUserbyId(id);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Map<String,String>> deleteUser(@PathVariable long id){
        return ResponseEntity.ok(authService.deleteUser(id));
    }


}
