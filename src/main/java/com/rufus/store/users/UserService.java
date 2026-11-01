package com.rufus.store.users;

import com.rufus.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final AuthService authService;


    public Page<UserDto> getAllUsers(Pageable pageable) {
        var currentUser = authService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You do not have access to this resource");
        }

        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    public UserDto getUser(Long userId) {
        checkSelfOrAdmin(userId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }

    public User registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException();
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return user;
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        checkSelfOrAdmin(userId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userMapper.update(request, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        checkSelfOrAdmin(userId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public List<AddressDto> getAddresses(Long userId) {
        checkSelfOrAdmin(userId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getAddresses().stream().map(this::toAddressDto).toList();
    }

    public AddressDto addAddress(Long userId, CreateAddressRequest request) {
        checkSelfOrAdmin(userId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        var address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZip(request.getZip());
        address.setUser(user);

        addressRepository.save(address);

        return toAddressDto(address);
    }

    private AddressDto toAddressDto(Address address) {
        var dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZip(address.getZip());
        return dto;
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        checkSelfOrAdmin(userId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    private void checkSelfOrAdmin(Long userId) {
        var currentUser = authService.getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You do not have access to this resource");
        }
    }
}