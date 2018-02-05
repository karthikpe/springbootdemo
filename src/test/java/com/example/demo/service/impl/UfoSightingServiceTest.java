package com.example.demo.service.impl;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.example.demo.dto.UfoSighting;
import com.example.demo.service.UfoSightingService;

/**
 * Test class for {@link UfoSightingServiceImpl}
 * 
 * @author kekambaram
 */
public class UfoSightingServiceTest {

	UfoSightingService ufoSightingService;

	@Before
	public void setUp() throws Exception {
		ufoSightingService = new UfoSightingServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
		ufoSightingService = null;
	}

	@Test
	public void testAllSightings() {
		List<UfoSighting> allSightings = ufoSightingService.getAllSightings();
		Assert.assertNotNull(allSightings);
		Assert.assertEquals(61391, allSightings.size());
	}

	@Test
	public void testUfoSightingSearch() {
		List<UfoSighting> ufoSightings = ufoSightingService.search(1995, 10);
		Assert.assertNotNull(ufoSightings);
		Assert.assertEquals(107, ufoSightings.size());
	}

	@Test
	public void testSearchUnavailableData() {
		List<UfoSighting> ufoSightings = ufoSightingService.search(2018, 10);
		Assert.assertNotNull(ufoSightings);
		Assert.assertEquals(0, ufoSightings.size());
	}

}
