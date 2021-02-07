package com.somecompany.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.somecompany.model.Entity;

/**
 * Services related to credit limit.
 * 
 * @author patrick
 */
@Service
public class CreditLimitService {

	@Value("${reportPath}")
	String reportPath;

	/**
	 * Generate credit limit report.
	 * 
	 * @param dataList
	 * @throws IOException
	 */
	public void generateCreditLimitReport(List<List<String>> dataList) throws IOException {

		// Key is entity name, value is the entity itself
		HashMap<String, Entity> entityInfoMap = new HashMap<>();

		// Stores the names of all root entities
		List<String> rootEntityList = new ArrayList<>();

		// Create Entity objects
		for (int i = 0; i < dataList.size(); i++) {

			if (i == 0) {
				// Is header row

				continue;
			}

			Entity entity = new Entity();

			entity.setName(dataList.get(i).get(0));

			String parent = dataList.get(i).get(1);
			if (parent.equals("")) {
				// Is a root entity

				rootEntityList.add(entity.getName());
			}
			entity.setParent(parent);

			entity.setLimit(Integer.valueOf(dataList.get(i).get(2)));
			entity.setUtilization(Integer.valueOf(dataList.get(i).get(3)));

			// Initialize listOfSubEntities
			List<String> entityList = new ArrayList<>();
			entity.setListOfSubEntities(entityList);

			// Initialize totalUtilization as current entity's utilization first
			entity.setTotalUtilization(Integer.valueOf(dataList.get(i).get(3)));

			entity.setTotalLimitOfSubEntities(0);

			entityInfoMap.put(entity.getName(), entity);
		}

		// Establish the group relationship
		for (Entry<String, Entity> entry : entityInfoMap.entrySet()) {

			String currentEntityName = entry.getKey();
			Entity currentEntity = entry.getValue();
			String parent = currentEntity.getParent();

			while (!parent.equals("")) {
				// Current entity has parent

				Entity parentEntity = entityInfoMap.get(parent);

				// Add current entity to its parent's listOfSubEntities
				List<String> currentParentSubEntities = parentEntity.getListOfSubEntities();

				if (!currentParentSubEntities.contains(currentEntityName)) {
					currentParentSubEntities.add(currentEntityName);
				}

				// Set parent's totalUtilization to include current entity's totalUtilization
				parentEntity
						.setTotalUtilization(parentEntity.getTotalUtilization() + currentEntity.getTotalUtilization());

				// Set parent's totalLimitOfSubEntities to include current entity's limit
				parentEntity.setTotalLimitOfSubEntities(
						parentEntity.getTotalLimitOfSubEntities() + currentEntity.getLimit());

				// Prepare for next loop
				parent = parentEntity.getParent();
			}
		}

		// Print report
		for (String currentRootEntityName : rootEntityList) {

			Entity currentRootEntity = entityInfoMap.get(currentRootEntityName);

			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(reportPath, true));

			/* Label */

			System.out.print("Entities: ");
			bufferedWriter.write("Entities: ");

			List<String> list = currentRootEntity.getListOfSubEntities();
			list.add(0, currentRootEntityName);

			for (int i = 0; i < list.size(); i++) {
				// Print the current subEntity
				System.out.print(list.get(i));
				bufferedWriter.write(list.get(i));

				if (i == list.size() - 1) {
					// Last subEntity

					System.out.print(":");
					bufferedWriter.write(":");
				} else {
					// Not last subEntity

					System.out.print("/");
					bufferedWriter.write("/");
				}
			}

			System.out.println();
			System.out.println();
			bufferedWriter.write("\n\n");

			/* Content */

			int totalUtilization = currentRootEntity.getTotalUtilization();
			int limit = currentRootEntity.getLimit();
			int utilization = currentRootEntity.getUtilization();
			int totalLimitOfSubEntities = currentRootEntity.getTotalLimitOfSubEntities();

			// Report on limit breach
			if (totalUtilization > limit) {
				// Limit breach

				System.out.print("Limit breach at " + currentRootEntityName);
				System.out.print(" (");
				System.out.print("limit = " + limit);
				System.out.print(", ");
				System.out.print("direct utilisation = " + utilization);
				System.out.print(", ");
				System.out.print("combined utilisation = " + totalUtilization);
				System.out.print(").");

				bufferedWriter.write("Limit breach at " + currentRootEntityName);
				bufferedWriter.write(" (");
				bufferedWriter.write("limit = " + limit);
				bufferedWriter.write(", ");
				bufferedWriter.write("direct utilisation = " + utilization);
				bufferedWriter.write(", ");
				bufferedWriter.write("combined utilisation = " + totalUtilization);
				bufferedWriter.write(").");
			} else {
				// No limit breach

				System.out.print("No limit breaches");
				bufferedWriter.write("No limit breaches");
			}

			System.out.println();
			System.out.println();
			bufferedWriter.write("\n\n");

			// Report on where the combined sub-entity limits are higher than the limits at the parent group(s)
			// (i.e.
			// limit warning)
			if (totalLimitOfSubEntities > limit) {
				// Limit warning

				System.out.print("Combined sub-entity limit is higher than the parent limit");
				System.out.print(" (");
				System.out.print("limit = " + limit);
				System.out.print(", ");
				System.out.print("Combined sub-entity limit = " + totalLimitOfSubEntities);
				System.out.print(").");

				bufferedWriter.write("Combined sub-entity limit is higher than the parent limit");
				bufferedWriter.write(" (");
				bufferedWriter.write("limit = " + limit);
				bufferedWriter.write(", ");
				bufferedWriter.write("Combined sub-entity limit = " + totalLimitOfSubEntities);
				bufferedWriter.write(").");
			}

			System.out.println();
			System.out.println();
			bufferedWriter.write("\n\n");

			bufferedWriter.close();

		}
	}
}
