package com.iankoski.api.mafinance.controller;

import com.iankoski.api.mafinance.entity.dto.AgentDTO;
import com.iankoski.api.mafinance.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agents")
public class AgentController {

	private final AgentService agentService;

	public AgentController(AgentService agentService) {
		this.agentService = agentService;
	}

	@PostMapping
	private ResponseEntity<String> create(@RequestBody AgentDTO agentDTO) {
		agentService.create(agentDTO);
		return ResponseEntity.ok("Agent created successfully");
	}
}
