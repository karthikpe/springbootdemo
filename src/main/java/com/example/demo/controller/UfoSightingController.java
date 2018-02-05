package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UfoSighting;
import com.example.demo.service.UfoSightingService;

@RestController
public class UfoSightingController {

	@Autowired
	UfoSightingService ufoSightingService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	List<String> getAll() {
		return ufoSightingService.getAllSightings().stream().map(ufo -> ufo.getDateSeen()).collect(Collectors.toList());
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	List<UfoSighting> searchUfoSightings(@RequestParam(value = "year", required = false) final Integer year,
			@RequestParam(value = "month", required = false) final Integer month) {
		return ufoSightingService.search(year == null ? 2000 : year, month == null ? 12 : month);
	}
}
