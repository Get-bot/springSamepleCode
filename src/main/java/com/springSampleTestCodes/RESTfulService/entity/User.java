package com.springSampleTestCodes.RESTfulService.entity;

import com.springSampleTestCodes.RESTfulService.dto.UserRegisterDTO;
import com.springSampleTestCodes.RESTfulService.dto.UserUpdateDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String email;

  private String password;

  public static User setUser(Long id, String username, String email, String password) {
    User user = new User();
    user.id = id;
    user.username = username;
    user.email = email;
    user.password = password;
    return user;
  }

  public static User setRegisterUser(UserRegisterDTO userRegisterDTO) {
    User user = new User();
    user.username = userRegisterDTO.getUsername();
    user.email = userRegisterDTO.getEmail();
    user.password = userRegisterDTO.getPassword();
    return user;
  }

  public void updateWithUpdateDTO(UserUpdateDTO userUpdateDTO) {
    this.email = userUpdateDTO.getEmail();
    if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().equals(this.password)) {
      this.password = userUpdateDTO.getPassword();
    }
  }

}
