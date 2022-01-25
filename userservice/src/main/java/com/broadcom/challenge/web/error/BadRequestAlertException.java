package com.broadcom.challenge.web.error;


import java.util.ArrayList;
import java.util.List;

public class BadRequestAlertException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    private List<String> errors = new ArrayList<>();



    public BadRequestAlertException(String message, String entityName, String errorKey) {
        errors.add(message);
        this.entityName = entityName;
        this.errorKey = errorKey;
    }


	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}


    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
