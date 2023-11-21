package com.springSampleTestCodes.RESTfulService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springSampleTestCodes.RESTfulService.dto.UserDTO;
import com.springSampleTestCodes.RESTfulService.dto.UserRegisterDTO;
import com.springSampleTestCodes.RESTfulService.dto.UserUpdateDTO;
import com.springSampleTestCodes.RESTfulService.entity.User;
import com.springSampleTestCodes.RESTfulService.exception.NotFoundException;
import com.springSampleTestCodes.RESTfulService.payload.ApiResponse;
import com.springSampleTestCodes.RESTfulService.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserApiController.class)
@WithMockUser(username = "테스트_최고관리자", roles = {"SUPER"})
@DisplayName("유저 API 테스트")
class UserApiControllerTest {
  private static final String API_URL = "/api/v1/users";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private UserService userService;

  @Test
  @DisplayName("유저 추가 API 실패 테스트")
  public void testAddShouldReturn400BadRequest() throws Exception {

    UserRegisterDTO userRegisterDTO = new UserRegisterDTO();

    userRegisterDTO.setUsername("");
    userRegisterDTO.setPassword("test1234");

    String requestBody = objectMapper.writeValueAsString(userRegisterDTO);

    mockMvc.perform(
            post(API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("유저 추가 API 성공 테스트")
  public void testAddShouldReturn200Request() throws Exception {

    UserRegisterDTO userRegisterDTO = new UserRegisterDTO();

    userRegisterDTO.setUsername("test");
    userRegisterDTO.setEmail("test1234@naver.com");
    userRegisterDTO.setPassword("test1234");

    String requestBody = objectMapper.writeValueAsString(userRegisterDTO);

    Mockito.when(userService.register(any(UserRegisterDTO.class)))
        .thenReturn(ResponseEntity.ok().body(ApiResponse.setApiResponse(true, "add user", UserDTO.setUserDTO(User.setRegisterUser(userRegisterDTO)))));

    mockMvc.perform(
            post(API_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.data.email").value(userRegisterDTO.getEmail()))
        .andExpect(jsonPath("$.data.username").value(userRegisterDTO.getUsername()))
        .andDo(print());
  }

  @Test
  @DisplayName("유저 조회 API 실패 테스트")
  void getUserTestShouldReturn404NotFound() throws Exception {
    Long userId = 1L;
    String requestUrl = API_URL + "/" + userId;

    when(userService.get(userId)).thenThrow(new NotFoundException("존재하지 않는 유저입니다."));

    mockMvc.perform(
            get(requestUrl)
                .with(csrf())
        )
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  @DisplayName("유저 조회 API 성공 테스트")
  void getUserTestShouldReturn200Request() throws Exception {
    Long userId = 1L;
    String requestURI = API_URL + "/" + userId;
    String email = "test@naver.com";

    User user = User.setUser(userId, "test", email, "test1234");
    ResponseEntity<ApiResponse<UserDTO>> responseEntity = ResponseEntity.ok().body(ApiResponse.setApiResponse(true, "get user", UserDTO.setUserDTO(user)));

    when(userService.get(userId)).thenReturn(responseEntity);

    mockMvc.perform(
            get(requestURI)
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.email").value(email))
        .andDo(print());
  }

  @Test
  @DisplayName("사용자 리스트 조회 API 성공 테스트")
  void getUserListShouldReturn200Request() throws Exception {
    String user1Email = "test1@naver.com";
    String user2Email = "test2@naver.com";
    User user1 = User.setUser(1L, "test1", user1Email, "test1234");
    User user2 = User.setUser(2L, "test2", user2Email, "test1234");

    List<User> userList = List.of(user1, user2);
    ResponseEntity<ApiResponse<List<UserDTO>>> responseEntity = ResponseEntity.ok().body(ApiResponse.setApiResponse(true, "userList find", userList.stream()
        .map(UserDTO::setUserDTO)
        .collect(Collectors.toList())));

    when(userService.getList()).thenReturn(responseEntity);

    mockMvc.perform(
            get(API_URL)
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.data[0].email").value(user1Email))
        .andExpect(jsonPath("$.data[1].email").value(user2Email))
        .andDo(print());
  }

  @Test
  @DisplayName("사용자 업데이트 API 실패 NotFound 테스트")
  void updateUserShouldReturn404NotFound() throws Exception {
    Long userId = 1L;
    String requestURI = API_URL + "/" + userId;

    UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
    userUpdateDTO.setEmail("test1@naver.com");
    userUpdateDTO.setPassword("test1234");

    String requestBody = objectMapper.writeValueAsString(userUpdateDTO);

    when(userService.update(eq(userId), any(UserUpdateDTO.class)))
        .thenThrow(new NotFoundException("해당하는 유저가 없습니다."));

    mockMvc.perform(
            put(requestURI)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  @DisplayName("사용자 업데이트 API 실패 BadRequest 테스트")
  void updateUserShouldReturn400BadRequest() throws Exception {
    Long userId = 1L;
    String requestURI = API_URL + "/" + userId;

    UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

    String requestBody = objectMapper.writeValueAsString(userUpdateDTO);

    mockMvc.perform(
            put(requestURI)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("사용자 업데이트 API 성공 테스트")
  void updateUserShouldReturn200Ok() throws Exception {
    Long userId = 1L;
    String requestURI = API_URL + "/" + userId;

    User user = User.setUser(userId, "test", "test@naver.com", "test1234");

    UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
    userUpdateDTO.setEmail("test@naver.com");
    userUpdateDTO.setPassword("test1234");

    Mockito.when(userService.update(eq(userId), any(UserUpdateDTO.class)))
        .thenReturn(ResponseEntity.ok().body(ApiResponse.setApiResponse(true, "user update", UserDTO.setUserDTO(user))));

    String requestBody = objectMapper.writeValueAsString(userUpdateDTO);

    mockMvc.perform(
            put(requestURI)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.email").value(user.getEmail()))
        .andDo(print());
  }

  @Test
  @DisplayName("사용자 업데이트 API 실패 NotFound 테스트")
  void deleteUserShouldReturn404NotFound() throws Exception {
    Long userId = 1L;
    String requestURI = API_URL + "/" + userId;

    when(userService.delete(userId))
        .thenThrow(new NotFoundException("해당하는 유저가 없습니다."));

    mockMvc.perform(
            delete(requestURI)
                .with(csrf())
        )
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  @DisplayName("사용자 삭제 API 성공 테스트")
  void deleteUserShouldReturn200Ok() throws Exception {
    Long userId = 1L;
    String requestURI = API_URL + "/" + userId;

    Mockito.when(userService.delete(userId))
        .thenReturn(ResponseEntity.ok().body(ApiResponse.setApiResponse(true, "delete user", null)));

    mockMvc.perform(
            delete(requestURI)
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

}