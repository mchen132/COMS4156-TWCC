package com.TWCC.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.TWCC.data.TwccUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsExt implements UserDetails {
    private static final long serialVersionUID = 1L;

	private int id;

    private String firstName;

    private String lastName;

	private String username;

	private String email;

	@JsonIgnore
	private String password;

	public UserDetailsExt(int id, String firstName, String lastName, String username, String email, String password) {
		this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public static UserDetailsExt build(TwccUser user) {
		return new UserDetailsExt(
				user.getId(),
                user.getFirstName(),
                user.getLastName(),
				user.getUsername(),
				user.getEmail(),
				user.getPassword());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	public int getId() {
		return id;
	}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsExt user = (UserDetailsExt) o;
		return Objects.equals(id, user.id);
	}
}
