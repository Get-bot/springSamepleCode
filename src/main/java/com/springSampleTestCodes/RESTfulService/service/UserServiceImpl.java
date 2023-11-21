package com.springSampleTestCodes.RESTfulService.service;

import com.springSampleTestCodes.RESTfulService.dto.UserDTO;
import com.springSampleTestCodes.RESTfulService.dto.UserRegisterDTO;
import com.springSampleTestCodes.RESTfulService.dto.UserUpdateDTO;
import com.springSampleTestCodes.RESTfulService.entity.User;
import com.springSampleTestCodes.RESTfulService.exception.NotFoundException;
import com.springSampleTestCodes.RESTfulService.payload.ApiResponse;
import com.springSampleTestCodes.RESTfulService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

  private final UserRepository userRepository;

  @Override
  public ResponseEntity<ApiResponse<UserDTO>> get(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(this::userNotFoundException);

    return ResponseEntity.ok().body(ApiResponse.setApiResponse(true, "get user", UserDTO.setUserDTO(user)));
  }

  @Override
  @Transactional
  public ResponseEntity<ApiResponse<UserDTO>> register(UserRegisterDTO userRegisterDTO) {
    checkIfUsernameExists(userRegisterDTO.getUsername());

    User user = userRepository.save(User.setRegisterUser(userRegisterDTO));
    URI url = URI.create("/api/v1/users/" + user.getId());
    return ResponseEntity.created(url).body(ApiResponse.setApiResponse(true, "add user", UserDTO.setUserDTO(user)));
  }

  @Override
  public ResponseEntity<ApiResponse<List<UserDTO>>> getList() {
    List<User> userList = userRepository.findAll();
    List<UserDTO> userDTOList = userList.stream()
        .map(UserDTO::setUserDTO)
        .collect(Collectors.toList());

    return ResponseEntity.ok().body(ApiResponse.setApiResponse(true, "userList find", userDTOList));
  }

  @Override
  @Transactional
  public ResponseEntity<ApiResponse<UserDTO>> update(Long id, UserUpdateDTO userUpdateDTO)  {

    User user = userRepository.findById(id)
        .orElseThrow(this::userNotFoundException);

    if (!user.getEmail().equals(userUpdateDTO.getEmail())) {
      checkIfEmailExists(userUpdateDTO.getEmail());
    }

    user.updateWithUpdateDTO(userUpdateDTO);
    return ResponseEntity.ok().body(ApiResponse.setApiResponse(true, "user update", UserDTO.setUserDTO(user)));
  }

  @Override
  @Transactional
  public ResponseEntity<ApiResponse<?>> delete(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(this::userNotFoundException);

    userRepository.delete(user);

    return ResponseEntity.ok().body(ApiResponse.setApiResponse(true, "delete user", null));
  }

  private void checkIfUsernameExists(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("이미 존재하는 유저 이름입니다.");
    }
  }

  private void checkIfEmailExists(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
    }
  }

  private NotFoundException userNotFoundException() {
    return new NotFoundException("존재하지 않는 유저입니다.");
  }

}
