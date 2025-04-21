package com.iankoski.api.mafinance.service;

import com.iankoski.api.mafinance.entity.Agent;
import com.iankoski.api.mafinance.entity.dto.AgentDTO;
import com.iankoski.api.mafinance.repository.AgentRepository;
import com.iankoski.api.mafinance.service.core.AgentUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AgentService implements AgentUseCase {

	private final AgentRepository repository;

	public AgentService(AgentRepository agentRepository) {
		this.repository = agentRepository;
	}

	@Override
	public void create(AgentDTO agentDTO) {
		Agent agent = Agent.builder()
				.name(agentDTO.name())
				.email(agentDTO.email())
				.createdAt(LocalDateTime.now())
				.active(true)
				.passwordHash(agentDTO.password())
				.build();

		repository.save(agent);
	}
}
