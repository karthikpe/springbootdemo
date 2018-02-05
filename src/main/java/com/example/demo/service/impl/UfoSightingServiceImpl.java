package com.example.demo.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.example.demo.constant.UfoConstants;
import com.example.demo.dto.UfoSighting;
import com.example.demo.exception.UfoServiceException;
import com.example.demo.service.UfoSightingService;

/**
 * Service Implementation for {@link UfoSightingService}
 * 
 * @author kekambaram
 */
@PropertySource("classpath:config.properties")
@Service
public class UfoSightingServiceImpl implements UfoSightingService {

	private static final Logger LOG = Logger.getLogger(UfoSightingServiceImpl.class.getName());

	@Value("${ufoFile}")
	private String filePath;

	@Override
	public List<UfoSighting> getAllSightings() {
		List<UfoSighting> ufoSightings = new ArrayList<>();
		try {
			ufoSightings = getUfoContent(getFilePath());
		} catch (UfoServiceException e) {
			LOG.warning(e.getMessage());
		}
		return ufoSightings;
	}

	@Override
	public List<UfoSighting> search(int yearSeen, int monthSeen) {
		List<UfoSighting> ufoSightings = new ArrayList<>();
		try {
			ufoSightings = getUfoContent(getFilePath());
		} catch (UfoServiceException e) {
			LOG.warning(e.getMessage());
			return Collections.emptyList();
		}
		final StringBuffer yrMonth = new StringBuffer();
		yrMonth.append(yearSeen);
		yrMonth.append(monthSeen);
		return ufoSightings.stream().filter(ufo -> ufo.getDateSeen().contains(yrMonth)).collect(Collectors.toList());
	}

	/**
	 * Method to read the file content and parse it to UfoSighting DTO.
	 * 
	 * @param filePath
	 * @return list of objects
	 */
	protected List<UfoSighting> getUfoContent(String filePath) throws UfoServiceException {
		final BufferedReader br = readFile(filePath);
		List<UfoSighting> ufoSightings = br.lines().map(line -> convertToUfoContent(line)).filter(Objects::nonNull)
				.collect(Collectors.toList());
		try {
			br.close();
		} catch (IOException e) {
			LOG.warning(e.getMessage());
		}
		return ufoSightings;
	}

	/**
	 * Method to read file content.
	 * 
	 * @param filePath
	 * @return buffered reader object
	 * @throws UfoServiceException
	 */
	private BufferedReader readFile(String filePath) throws UfoServiceException {
		final File file = new File(filePath);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new UfoServiceException("File " + filePath + " cannot be located");
		}
		return new BufferedReader(new InputStreamReader(is));
	}

	/**
	 * Method to parse the line and create a DTO.
	 * 
	 * @param line
	 * @return an object or null in case of parsing error
	 */
	private UfoSighting convertToUfoContent(String line) {
		String[] values = line.split(UfoConstants.FILE_SEPARATOR);
		UfoSighting ufoSighting = null;
		try {
			ufoSighting = new UfoSighting(values[0], values[1], values[2], values[3], values[4], values[5]);
		} catch (ArrayIndexOutOfBoundsException e) {
			LOG.warning("Parsing error in line :" + line);
		}
		return ufoSighting;
	}

	private String getFilePath() {
		return filePath == null ? UfoConstants.UFO_FILEPATH : filePath;
	}

}
