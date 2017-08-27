package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_ROLE")
@Data
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
public class UserRole implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ROLE_IDX")
	private int userRoleIdx;

	@Column(name = "ROLE", nullable = false, length = 45)
	private String role;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "UNIQUE_ID", nullable = false)
	@JsonBackReference
	private User user;
}
