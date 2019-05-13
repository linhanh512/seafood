package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;

/**
 * Represents a seller (a subclass of Customer)
 *
 */
@DClass(schema = "seafoodman")
public class Seller extends Customer {
	
	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	public Seller(@AttrRef("name") String name, @AttrRef("phone") String phone, 
			@AttrRef("address") Country address, @AttrRef("email") String email) {
		this(null, name, phone, address, email);
	}

	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public Seller(String id, String name, String phone, Country address, String email) 
		throws ConstraintViolationException {
		super(id, name, phone, address, email);
	}
	
	
}
