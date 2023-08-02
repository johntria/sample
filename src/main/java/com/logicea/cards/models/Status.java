package com.logicea.cards.models;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Statuses which a card can have.
 */
public enum Status {
	TODO("TODO"),
	INPROGRESS("INPROGRESS"),
	DONE("DONE");
	String value;

	Status(String value) {
		this.value = value;
	}

	public String getValue() {
		if(StringUtils.isEmpty(value)){
			return null;
		}
		return value;
	}

	public static String getValueByStatus(Status status){
		if(ObjectUtils.isEmpty(status)){
			return null;
		}
		return status.getValue();
	}
}