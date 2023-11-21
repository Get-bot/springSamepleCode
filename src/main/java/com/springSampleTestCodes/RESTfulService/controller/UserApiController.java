package com.springSampleTestCodes.RESTfulService.controller;

import com.springSampleTestCodes.RESTfulService.dto.UserDTO;
import com.springSampleTestCodes.RESTfulService.dto.UserRegisterDTO;
import com.springSampleTestCodes.RESTfulService.dto.UserUpdateDTO;
import com.springSampleTestCodes.RESTfulService.payload.ApiResponse;
import com.springSampleTestCodes.RESTfulService.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {

  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<UserDTO>> getUser(@NotNull @PathVariable("id") Long id) {
    return userService.get(id);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<UserDTO>>> getUserList() {
    return userService.getList();
  }

  @PostMapping
  public ResponseEntity<ApiResponse<UserDTO>> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
    return userService.register(userRegisterDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<UserDTO>> updateUser(@NotNull @PathVariable("id") Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
    return userService.update(id, userUpdateDTO);
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<?>> deleteUser(@NotNull @PathVariable("id") Long id) {
    return userService.delete(id);
  }


}
