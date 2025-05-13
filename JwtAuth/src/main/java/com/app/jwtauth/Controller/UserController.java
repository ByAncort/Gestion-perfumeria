package com.app.jwtauth.Controller;


import com.app.jwtauth.Models.User;
import com.app.jwtauth.Payloads.NoAuthResponse;
import com.app.jwtauth.Service.UserService;
import com.app.jwtauth.Service.VerifyService;
import com.app.jwtauth.dto.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private VerifyService verifyService;

    @GetMapping("/info/profile")
    public Object infoProfile(Authentication authentication) {
//        if (!hasPermission(authentication, "READ_PERMISSIONS") &&
//                !hasPermission(authentication, "ADMIN_PERMISSIONS")) {
//            return NoAuthResponse.builder()
//                    .errorCode(HttpStatus.UNAUTHORIZED.value())
//                    .Mensaje("USUARIO NO TIENE PERMISOS NECESARIOS")
//                    .build();
//        }
        return userService.getLoggedInUser();
    }

    private boolean hasPermission(Authentication auth, String permission) {
        return auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals(permission));
    }

    @GetMapping("/all")
    public Object AsignarRole(Authentication authentication) {

        return userService.getAllUser();
    }

    @GetMapping("/api/test-connection")
    public HttpStatus userAccess() {
        return HttpStatus.OK;
    }

    @PutMapping("/{userId}/assign-role")
    public ResponseEntity<?> assignRole(@PathVariable Long userId, @RequestParam String roleName) {
        ServiceResult<User> result = userService.assignRoleToUser(userId, roleName);

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getErrors());
        }

        return ResponseEntity.ok(result.getData());
    }
}
