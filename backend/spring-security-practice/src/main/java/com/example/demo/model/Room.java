package com.example.demo.model;

import lombok.Data;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

/**
 * Created by whilemouse on 17. 8. 24.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDX")
    private long idx;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARTICIPANT_LIMIT")
    private int participantLimit;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Participant> participantList;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Message> messageList;

    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    @OneToOne
    @CreatedBy
    private AppUser createdUser;

    @CreatedDate
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTimeAndZone")
    @Columns(columns={@Column(name = "CREATED_DATE"), @Column(name = "CREATED_DATE_TIMEZONE")})
    private DateTime createdDate;

    @OneToOne
    @LastModifiedBy
    private AppUser lastModifiedUser;

    @LastModifiedDate
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTimeAndZone")
    @Columns(columns={@Column(name = "LAST_MODIFIED_DATE"), @Column(name = "LAST_MODIFIED_DATE_TIMEZONE")})
    private DateTime lastModifiedDate;
}
