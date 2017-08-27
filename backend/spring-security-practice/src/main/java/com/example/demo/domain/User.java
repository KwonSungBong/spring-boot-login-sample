package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "USER")
@DynamicUpdate
public class User implements Serializable{

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Type(type="uuid-char")
	@Column(name = "UNIQUE_ID")
	private UUID uniqueId;

	@Column(name = "EMAIL", length = 150, unique = true, updatable = false)
	@Email
	private String email;

	@Column(name = "PASSWORD", length = 200, nullable = false)
	private String password;

	@Column(name = "ENABLED", nullable = false)
	private boolean enabled = true;

	@Column(name = "USER_NAME")
	private String userName;

	@CreatedDate
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTimeAndZone")
	@Columns(columns={@Column(name = "CREATED_DATE", nullable = false, updatable = false), @Column(name = "CREATED_DATE_TIMEZONE", nullable = false, updatable = false)})
	private DateTime createdDate;

	@LastModifiedDate
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTimeAndZone")
	@Columns(columns={@Column(name = "LAST_MODIFIED_DATE"), @Column(name = "LAST_MODIFIED_DATE_TIMEZONE") })
	private DateTime lastModifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	@JsonManagedReference
	private List<UserRole> userRoleList;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private UserSocial userSocial;

}
