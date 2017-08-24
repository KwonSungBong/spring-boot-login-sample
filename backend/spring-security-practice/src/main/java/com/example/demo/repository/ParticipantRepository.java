package com.example.demo.repository;

import com.example.demo.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by whilemouse on 17. 8. 24.
 */
public interface ParticipantRepository extends JpaRepository<Participant, Long>, JpaSpecificationExecutor {
}
