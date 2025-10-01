package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;


import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class User {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long userId;

     @Column(nullable = false)
     private String userName;

     @Column(nullable = false,unique = true)
     private String email;

     @Column(nullable = false)
     private String password;

     @CreationTimestamp
     private LocalDateTime createdAt;

     @UpdateTimestamp
     private LocalDateTime updateAt;


     @ManyToMany(fetch = FetchType.EAGER)
     @JoinTable(
             name = "user_roles",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id")
     )
     private Set<Role> roles = new HashSet<>();


     public User() {
     }

     public User(Long userId, String userName, String email, String password, LocalDateTime createdAt, LocalDateTime updateAt, Set<Role> roles) {
          this.userId = userId;
          this.userName = userName;
          this.email = email;
          this.password = password;
          this.createdAt = createdAt;
          this.updateAt = updateAt;
          this.roles = roles;
     }

     public Long getUserId() {
          return userId;
     }

     public void setUserId(Long userId) {
          this.userId = userId;
     }

     public String getUserName() {
          return userName;
     }

     public void setUserName(String userName) {
          this.userName = userName;
     }

     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }

     public String getPassword() {
          return password;
     }

     public void setPassword(String password) {
          this.password = password;
     }

     public LocalDateTime getCreatedAt() {
          return createdAt;
     }

     public void setCreatedAt(LocalDateTime createdAt) {
          this.createdAt = createdAt;
     }

     public LocalDateTime getUpdateAt() {
          return updateAt;
     }

     public void setUpdateAt(LocalDateTime updateAt) {
          this.updateAt = updateAt;
     }

     public Set<Role> getRoles() {
          return roles;
     }

     public void setRoles(Set<Role> roles) {
          this.roles = roles;
     }
}
