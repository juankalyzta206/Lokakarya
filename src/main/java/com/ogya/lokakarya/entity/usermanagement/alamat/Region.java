/*
* SubMenu.java
*	This class is sub menu entity/table, getter and setter for each column
*
* Version 1.0
*
* Copyright : Irzan Maulana, Backend Team OGYA
*/
package com.ogya.lokakarya.entity.usermanagement.alamat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "REGION")
public class Region {
	private Long regionId;
	private String nama;
	private String programName;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;

	@Id
	@GeneratedValue(generator = "REGION_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "REGION_GEN", sequenceName = "REGION_SEQ", initialValue = 1, allocationSize = 1)
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long subMenuId) {
		this.regionId = subMenuId;
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
	@Column(name = "PROGRAM_NAME")
	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	// --------------------------------------------------------------------------------------------------------
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	// --------------------------------------------------------------------------------------------------------
	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	// --------------------------------------------------------------------------------------------------------
	@Column(name = "UPDATED_DATE")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	// --------------------------------------------------------------------------------------------------------
	@Column(name = "UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	@PrePersist
	private void onCreate() {
		createdDate = new Date();
	}

}
