package com.binhan.flightmanagement.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;
    private String password;

    private String phone;
    @Lob
    @Column(name = "imagedata")
    private byte[] imageData;

    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
                joinColumns = @JoinColumn(name = "user_id",nullable = false, referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "role_id",nullable = false, referencedColumnName = "id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities= new ArrayList<>();
        for(RoleEntity role : this.getRoles()){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
