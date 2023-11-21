package com.springSampleTestCodes.RESTfulService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

  @NotEmpty
  private String username;

  @Email
  @NotEmpty
  private String email;

  @NotEmpty
  private String password;

  public static UserRegisterDTO setUserRegisterDTO(String username, String email, String password) {
    UserRegisterDTO user = new UserRegisterDTO();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    return user;
  }
}
