package com.tenmo.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.tenmo.util.TenmoRoles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class User {

   private Long userId;
   private String username;
   @JsonIgnore // prevent from being sent to client
   private String password;
   private String email;
   @JsonIgnore
   private boolean activated;
   private LocalDateTime createdAt;
   private Set<Authority> authorities = new HashSet<>();


   public User(
           Long userId,
           String username,
           String password,
           String email,
           String authorities) {
      this.userId = userId;
      this.username = username;
      this.password = password;
      if (authorities != null) this.setAuthorities(authorities);
      this.activated = true;
   }

   public void setAuthorities(String authorities) {
      String[] roles = authorities.split(",");
      for (String role : roles) {
         if(TenmoRoles.contains(role))
            this.authorities.add(new Authority(role));
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return userId == user.userId &&
              activated == user.activated &&
              Objects.equals(username, user.username) &&
              Objects.equals(password, user.password) &&
              Objects.equals(authorities, user.authorities);
   }

   @Override
   public int hashCode() {
      return Objects.hash(userId, username, password, activated, authorities);
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + userId +
              ", username='" + username + '\'' +
              ", activated=" + activated +
              ", authorities=" + authorities +
              '}';
   }
}
