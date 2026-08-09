package com.rufus.store.users;

import com.rufus.store.auth.JwtConfig;
import com.rufus.store.auth.JwtResponse;
import com.rufus.store.auth.JwtService;
import com.rufus.store.common.ApiResponse;
import com.rufus.store.common.PaginationInfo;
import com.rufus.store.common.ResponseMeta;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sort", defaultValue = "name") String sortBy) {

        if (!Set.of("name", "email").contains(sortBy)) sortBy = "name";

        Page<UserDto> userPage = userService.getAllUsers(
                PageRequest.of(page - 1, size, Sort.by(sortBy))
        );

        ResponseMeta meta = ResponseMeta.builder()
                .pagination(PaginationInfo.builder()
                        .currentPage(page)
                        .pageSize(size)
                        .totalItems(userPage.getTotalElements())
                        .totalPages(userPage.getTotalPages())
                        .build())
                .build();

        return ResponseEntity.ok(new ApiResponse<>(userPage.getContent(), meta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ApiResponse<>(userService.getUser(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JwtResponse>> registerUser(
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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(new JwtResponse(accessToken.toString())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(userService.updateUser(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable("id") Long id,
            @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<ApiResponse<List<AddressDto>>> getAddresses(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ApiResponse<>(userService.getAddresses(id)));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<ApiResponse<AddressDto>> addAddress(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateAddressRequest request,
            UriComponentsBuilder uriBuilder) {

        var addressDto = userService.addAddress(id, request);
        var uri = uriBuilder.path("/users/{userId}/addresses/{addressId}")
                .buildAndExpand(id, addressDto.getId()).toUri();

        return ResponseEntity.created(uri).body(new ApiResponse<>(addressDto));
    }
}