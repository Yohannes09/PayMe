package com.tenmo.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Authority {

   private String role;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Authority authority = (Authority) o;
      return role.equals(authority.role);
   }

   @Override
   public int hashCode() {
      return Objects.hash(role);
   }

   @Override
   public String toString() {
      return "Authority{" +
         "name=" + role +
         '}';
   }
}
