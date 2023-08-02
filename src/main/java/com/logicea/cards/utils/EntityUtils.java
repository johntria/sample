package com.logicea.cards.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import com.logicea.cards.exceptions.InvalidCriteria;
import com.logicea.cards.requests.SortByDTO;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;

/**
 * Utility class containing  methods related to entities in the application.
 * This class provides methods to retrieve column names and join column names of a given entity class,
 * as well as a method to validate and group sorting criteria for the entity.
 */
public final class EntityUtils {
	private EntityUtils() {
	}

	/**
	 * Retrieves the column names of a given entity class.
	 *
	 * @param clazz The Class object representing the entity.
	 * @return A list of column names as strings.
	 */
	public static List<String> getColumnNames(Class<?> clazz) {
		List<String> columnNames = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				Column columnAnnotation = field.getAnnotation(Column.class);
				String columnName = columnAnnotation.name();
				columnNames.add(columnName);
			}
		}

		return columnNames;
	}

	/**
	 * Retrieves the join column names of a given entity class.
	 *
	 * @param clazz The Class object representing the entity.
	 * @return A list of join column names as strings.
	 */
	public static List<String> getJoinColumnNames(Class<?> clazz) {
		List<String> joinColumnNames = new ArrayList<>();
		// Get all declared fields of the class
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			if (field.isAnnotationPresent(JoinColumn.class)) {
				JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);
				String joinColumnName = joinColumnAnnotation.name();
				joinColumnNames.add(joinColumnName);
			}
		}

		return joinColumnNames;
	}

	/**
	 * Retrieves the join column names & column names of a given entity class.
	 *
	 * @param clazz The Class object representing the entity.
	 * @return A list of column names as strings.
	 */
	public static List<String> getColumnAndJoinColumnNames(Class<?> clazz) {
		return Stream.concat(getJoinColumnNames(clazz).stream(), getColumnNames(clazz).stream()).collect(Collectors.toList());
	}

	/**
	 * Validates and groups sorting criteria for the entity based on the provided SortByDTO list.
	 *
	 * @param sortMap The list of SortByDTO objects representing the sorting criteria.
	 * @param clazz   The Class object representing the entity to validate the sorting criteria against.
	 * @return A Sort object representing the validated and grouped sorting criteria.
	 * @throws InvalidCriteria If the sorting criteria contain invalid field names or directions.
	 */
	public static Sort validateAndGroupAllSorts(List<SortByDTO> sortMap, Class<?> clazz) {
		Sort sortable = Sort.unsorted();
		if (CollectionUtils.isEmpty(sortMap)) {
			return sortable;
		}
		List<String> columnAndJoinColumnNames = getColumnAndJoinColumnNames(clazz);
		for (SortByDTO eachElement : sortMap) {
			if (!columnAndJoinColumnNames.contains(eachElement.getFieldName())) {
				throw new InvalidCriteria(String.format("Given fieldName is not correct %s. Accepted keys:%s",
						eachElement.getFieldName(), columnAndJoinColumnNames));
			}
			Sort.Direction direction = Sort.Direction.fromOptionalString(eachElement.getDirection()).orElseThrow(
					() -> new InvalidCriteria(String.format("Given entry value is not correct%s. Accepted values:%s",
							eachElement.getDirection(), Sort.Direction.values()))
			);
			sortable.and(Sort.by(direction, eachElement.getFieldName()));
		}
		return sortable;
	}
}