package com.somecompany;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.somecompany.service.CreditLimitService;

@SpringBootTest
@ActiveProfiles("test")
public class CreditLimitTest {

	@Autowired
	private CreditLimitService creditLimitService;

	@Test
	public void shouldBeAbleToReadAFile() {

		// Actual result
		List<List<String>> resultList = creditLimitService.readCSV();

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
}
