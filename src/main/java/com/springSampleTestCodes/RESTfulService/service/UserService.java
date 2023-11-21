package com.springSampleTestCodes.RESTfulService.service;

import com.springSampleTestCodes.RESTfulService.dto.UserDTO;
import com.springSampleTestCodes.RESTfulService.dto.UserRegisterDTO;
import com.springSampleTestCodes.RESTfulService.dto.UserUpdateDTO;
import com.springSampleTestCodes.RESTfulService.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

  ResponseEntity<ApiResponse<UserDTO>> get(Long id);

  ResponseEntity<ApiResponse<UserDTO>> register(UserRegisterDTO userRegisterDTO);

  ResponseEntity<ApiResponse<List<UserDTO>>> getList();

  ResponseEntity<ApiResponse<UserDTO>> update(Long id, UserUpdateDTO userUpdateDTO);

  ResponseEntity<ApiResponse<?>> delete(Long id);

}
