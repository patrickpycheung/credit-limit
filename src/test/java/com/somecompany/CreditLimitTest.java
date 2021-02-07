package com.somecompany;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.somecompany.service.CreditLimitService;
import com.somecompany.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class CreditLimitTest {

	@Autowired
	private CreditLimitService creditLimitService;

	@Autowired
	private UtilService utilService;

	@Value("${dataPath}")
	String dataPath;

	@Value("${reportPath}")
	String reportPath;

	@Value("${expectedReportPath}")
	String expectedReportPath;

	@Test
	public void shouldBeAbleToReadAFile() throws IOException {

		// Actual result
		List<List<String>> resultList = utilService.readCSV(dataPath);

		// Expected result
		List<List<String>> expectedList = new ArrayList<>();

		ArrayList<String> list = new ArrayList<>();

		list.add("entity");
		list.add("parent");
		list.add("limit");
		list.add("utilisation");
		expectedList.add(list);

		list = new ArrayList<>();
		list.add("A");
		list.add("");
		list.add("100");
		list.add("0");
		expectedList.add(list);

		list = new ArrayList<>();
		list.add("B");
		list.add("A");
		list.add("90");
		list.add("10");
		expectedList.add(list);

		list = new ArrayList<>();
		list.add("C");
		list.add("B");
		list.add("40");
		list.add("20");
		expectedList.add(list);

		list = new ArrayList<>();
		list.add("D");
		list.add("B");
		list.add("40");
		list.add("30");
		expectedList.add(list);

		list = new ArrayList<>();
		list.add("E");
		list.add("");
		list.add("200");
		list.add("150");
		expectedList.add(list);

		list = new ArrayList<>();
		list.add("F");
		list.add("E");
		list.add("100");
		list.add("80");
		expectedList.add(list);

		// Assertion
		assertEquals(expectedList, resultList);
	}

	@Test
	public void shouldBeAbleToGenerateCreditLimitReport() throws IOException {

		// Actual result
		List<List<String>> dataList = utilService.readCSV(dataPath);
		creditLimitService.generateCreditLimitReport(dataList);

		// Assertion
		Path expectedReportPathObj = Paths.get(expectedReportPath);
		Path reportPathObj = Paths.get(reportPath);

		try (InputStream exptectedInputStream = Files.newInputStream(expectedReportPathObj);
				InputStream actualInputStream = Files.newInputStream(reportPathObj)) {

			int expectedData;

			try {
				// Check that all the expected data exists in the result as well
				while ((expectedData = exptectedInputStream.read()) != -1) {
					// Compare byte-by-byte
					assertEquals(expectedData, actualInputStream.read());
				}
			} catch (IOException ioe) {
				log.error("I/O error occurred with file");
			}
		} catch (IOException ioe) {
			log.error("I/O error occurred with file");
		}

		// Format the report after test
		utilService.removeFileContent(reportPath);
	}
}
