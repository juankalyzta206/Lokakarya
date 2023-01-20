package com.ogya.lokakarya.usermanagement.entity.login;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
public class RolesLogin {
	private Long roleId;
	private String nama;
	private Set<RoleMenuLogin> roleMenu = new HashSet<RoleMenuLogin>(0);

	@Id
	@GeneratedValue(generator = "ROLES_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ROLES_GEN", sequenceName = "ROLES_SEQ", initialValue = 1, allocationSize = 1)
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	// --------------------------------------------------------------------------------------------------------
	@Column(name = "NAMA")
	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	// --------------------------------------------------------------------------------------------------------
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	public Set<RoleMenuLogin> getRoleMenu() {
		return this.roleMenu;
	}

	public void setRoleMenu(Set<RoleMenuLogin> roleMenu) {
		this.roleMenu = roleMenu;
	}

}
