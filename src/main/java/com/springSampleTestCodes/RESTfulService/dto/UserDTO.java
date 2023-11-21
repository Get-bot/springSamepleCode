package com.springSampleTestCodes.RESTfulService.dto;

import com.springSampleTestCodes.RESTfulService.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private Long id;
  private String username;
  private String email;

  public static UserDTO setUserDTO(User user) {
    return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
  }

}
