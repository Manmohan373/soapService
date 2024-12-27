package com.alfaris.ipsh.soap.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.alfaris.ipsh.soap.entity.Employee;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
@Slf4j
public class EmployeeSpecification {

    public static Specification<Employee> getStreamBySearchSpec(String searchParam) {
        return new Specification<Employee>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate finalPredicate = null;
                JSONParser parser = new JSONParser();
                JSONObject searchObject;
                String startDate = "";
                String endDate = "";

                try {
                    if (StringUtils.hasLength(searchParam)) {
                        searchObject = (JSONObject) parser.parse(searchParam);

                        // Extract search parameters from the JSON object
                        String empId = (String) searchObject.get("empId");
                        String department = (String) searchObject.get("department");
                        String name = (String) searchObject.get("name");
                        String salary = (String) searchObject.get("salary");
                        String status = (String) searchObject.get("status");
                        String dateRange = (String) searchObject.get("doj");

                        // Handle date range parsing
                        if (StringUtils.hasLength(dateRange)) {
                            String[] date = dateRange.split("-");
                            startDate = date[0].trim();
                            endDate = date[1].trim();
                        }

                        // Build predicates based on the extracted parameters
                        if (StringUtils.hasLength(startDate)) {
                            Predicate startPredicate = buildDatePredicate(criteriaBuilder, root, startDate, true);
                            finalPredicate = combine(finalPredicate, startPredicate, criteriaBuilder);
                        }
                        if (StringUtils.hasLength(endDate)) {
                            Predicate endPredicate = buildDatePredicate(criteriaBuilder, root, endDate, false);
                            finalPredicate = combine(finalPredicate, endPredicate, criteriaBuilder);
                        }
                        finalPredicate = combine(finalPredicate, stringPredicate(criteriaBuilder, root, "empId", empId), criteriaBuilder);
                        finalPredicate = combine(finalPredicate, stringPredicate(criteriaBuilder, root, "name", name), criteriaBuilder);
                        finalPredicate = combine(finalPredicate, stringPredicate(criteriaBuilder, root, "department", department), criteriaBuilder);
                        finalPredicate = combine(finalPredicate, stringPredicate(criteriaBuilder, root, "salary", salary), criteriaBuilder);
                        finalPredicate = combine(finalPredicate, stringPredicate(criteriaBuilder, root, "status", status), criteriaBuilder);
                    }

                    query.orderBy(criteriaBuilder.desc(root.get("status")));
                } catch (Exception e) {
                    log.error("Error in creating specification: " + e.getMessage());
                }

                return finalPredicate;
            }
        };
    }

    // Helper method to build a date-based predicate (either start or end date)
    private static Predicate buildDatePredicate(CriteriaBuilder criteriaBuilder, Root<Employee> root, String dateStr, boolean isStartDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = format.parse(dateStr);
            String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
            Date finalDate = format.parse(formattedDate);

            return isStartDate
                ? criteriaBuilder.greaterThanOrEqualTo(root.get("doj"), finalDate)
                : criteriaBuilder.lessThanOrEqualTo(root.get("doj"), finalDate);
        } catch (ParseException e) {
            log.error("Date parsing error for " + dateStr + ": " + e.getMessage());
        }
        return null;
    }

    // Helper method to create a LIKE predicate for string fields
    private static Predicate stringPredicate(CriteriaBuilder criteriaBuilder, Root<Employee> root, String field, String value) {
        if (StringUtils.hasLength(value)) {
            return criteriaBuilder.like(root.get(field), "%" + value + "%");
        }
        return null;
    }

    // Helper method to combine the predicates (AND logic)
    private static Predicate combine(Predicate existingPredicate, Predicate newPredicate, CriteriaBuilder criteriaBuilder) {
        if (newPredicate != null) {
            return (existingPredicate == null) ? newPredicate : criteriaBuilder.and(existingPredicate, newPredicate);
        }
        return existingPredicate;
    }
}
