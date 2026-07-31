package com.rufus.store.users;

import com.rufus.store.auth.JwtConfig;
import com.rufus.store.auth.JwtResponse;
import com.rufus.store.auth.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        return userService.getAllUsers(sortBy);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<JwtResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            HttpServletResponse response) {

        var user = userService.registerUser(request);

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(accessToken.toString()));
    }

    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/change-password")
    public void changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
    }

    @GetMapping("/{id}/addresses")
    public List<AddressDto> getAddresses(@PathVariable Long id) {
        return userService.getAddresses(id);
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<AddressDto> addAddress(
            @PathVariable Long id,
            @Valid @RequestBody CreateAddressRequest request,
            UriComponentsBuilder uriBuilder) {

        var addressDto = userService.addAddress(id, request);
        var uri = uriBuilder.path("/users/{userId}/addresses/{addressId}")
                .buildAndExpand(id, addressDto.getId()).toUri();

        return ResponseEntity.created(uri).body(addressDto);
    }
}