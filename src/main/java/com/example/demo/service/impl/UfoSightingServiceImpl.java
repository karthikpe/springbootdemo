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
import org.springframework.util.StringUtils;

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

	@Value("${fileName}")
	private String srcFile;

	@Override
	public List<UfoSighting> getAllSightings() {
		List<UfoSighting> ufoSightings = new ArrayList<>();
		try {
			ufoSightings = getUfoContent();
		} catch (UfoServiceException e) {
			LOG.warning(e.getMessage());
		}
		return ufoSightings;
	}

	@Override
	public List<UfoSighting> search(int yearSeen, int monthSeen) {
		final StringBuffer yrMonth = new StringBuffer();
		yrMonth.append(yearSeen);
		yrMonth.append(monthSeen < 10 ? UfoConstants.ZERO + monthSeen : monthSeen);
		List<UfoSighting> ufoSightings = new ArrayList<>();
		try {
			ufoSightings = getUfoContent(yrMonth.toString());
		} catch (UfoServiceException e) {
			LOG.warning(e.getMessage());
			return Collections.emptyList();
		}
		return ufoSightings;
	}

	/**
	 * Method to read the file content and convert it to UfoSighting DTO.
	 * 
	 * @return list of objects
	 */
	protected List<UfoSighting> getUfoContent() throws UfoServiceException {
		return getUfoContent(null);
	}

	/**
	 * Method to read and search the file content and convert it to UfoSighting DTO.
	 * 
	 * @param yrMonth
	 * @return list of objects
	 */
	protected List<UfoSighting> getUfoContent(String yrMonth) throws UfoServiceException {
		List<UfoSighting> ufoSightings = new ArrayList<>();
		final BufferedReader br = readFile();
		try {
			ufoSightings = br.lines()
					.map(line -> convertToUfoContent(line, yrMonth))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		} catch (Exception e) {
			LOG.warning(e.getMessage());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				LOG.warning(e.getMessage());
			}
		}
		return ufoSightings;
	}

	/**
	 * Method to read file content.
	 * 
	 * @return buffered reader object
	 * @throws UfoServiceException
	 */
	private BufferedReader readFile() throws UfoServiceException {
		File file = (srcFile == null) ? new File(UfoConstants.UFO_FILEPATH)
				: new File(getClass().getClassLoader().getResource(srcFile).getFile());
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new UfoServiceException("File " + file.getName() + " cannot be read");
		}
		return new BufferedReader(new InputStreamReader(is));
	}

	/**
	 * Method to parse the line and create a DTO.
	 * 
	 * @param line
	 * @param yrMonth
	 * @return an object or null in case of parsing error
	 */
	private UfoSighting convertToUfoContent(String line, String yrMonth) {
		String[] values = line.split(UfoConstants.FILE_SEPARATOR);
		if (!StringUtils.isEmpty(yrMonth) && !values[0].contains(yrMonth)) {
			return null;
		}
		UfoSighting ufoSighting = null;
		try {
			ufoSighting = new UfoSighting(values[0], values[1], values[2], values[3], values[4], values[5]);
		} catch (ArrayIndexOutOfBoundsException e) {
			LOG.warning("Parsing error in line :" + line);
		}
		return ufoSighting;
	}

}
