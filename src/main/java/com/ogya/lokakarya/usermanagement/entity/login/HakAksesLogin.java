package com.ogya.lokakarya.usermanagement.entity.login;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ogya.lokakarya.usermanagement.entity.Users;

@Entity
@Table(name = "HAK_AKSES")
public class HakAksesLogin {
	private Long hakAksesId;
	private Users users;
	private RolesLogin roles;

	@Id
	@GeneratedValue(generator = "HAK_AKSES_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "HAK_AKSES_GEN", sequenceName = "HAK_AKSES_SEQ", initialValue = 1, allocationSize = 1)
	public Long getHakAksesId() {
		return hakAksesId;
	}

	public void setHakAksesId(Long hakAksesId) {
		this.hakAksesId = hakAksesId;
	}

	// --------------------------------------------------------------------------------------------------------
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	@JsonIgnore
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	// --------------------------------------------------------------------------------------------------------
	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	public RolesLogin getRoles() {
		return roles;
	}

	public void setRoles(RolesLogin roles) {
		this.roles = roles;
	}

}
