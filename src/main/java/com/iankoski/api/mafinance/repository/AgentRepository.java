package com.iankoski.api.mafinance.repository;

import com.iankoski.api.mafinance.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
	Agent findByEmail(String email);
	Agent findByName(String name);
	Optional<Agent> findById(Long id);
}
