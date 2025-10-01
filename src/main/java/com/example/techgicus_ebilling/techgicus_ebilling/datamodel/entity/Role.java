package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "roles")
public class Role {

       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long roleId;

       @Column(nullable = false)
       private String roleName;

       @Column(length = 100)
       private String description;

       @Column(name = "is_system_role")
       private boolean systemRole = false; // created at time of system intialzed this called system role

       @ManyToMany(mappedBy = "roles")
       private Set<User> users = new HashSet<>();

       public Role() {
       }

       public Role(Long roleId, String roleName, String description, boolean systemRole, Set<User> users) {
              this.roleId = roleId;
              this.roleName = roleName;
              this.description = description;
              this.systemRole = systemRole;
              this.users = users;
       }

       public Long getRoleId() {
              return roleId;
       }

       public void setRoleId(Long roleId) {
              this.roleId = roleId;
       }

       public String getRoleName() {
              return roleName;
       }

       public void setRoleName(String roleName) {
              this.roleName = roleName;
       }

       public String getDescription() {
              return description;
       }

       public void setDescription(String description) {
              this.description = description;
       }

       public boolean isSystemRole() {
              return systemRole;
       }

       public void setSystemRole(boolean systemRole) {
              this.systemRole = systemRole;
       }

       public Set<User> getUsers() {
              return users;
       }

       public void setUsers(Set<User> users) {
              this.users = users;
       }
}
