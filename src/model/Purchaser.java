package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;

@DClass(schema = "seafoodman")
public class Purchaser extends Customer {
	
	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	public Purchaser(@AttrRef("name") String name, @AttrRef("phone") String phone, @AttrRef("address") Country address,
			@AttrRef("email") String email) {
		this(null, name, phone, address, email);
	}

	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public Purchaser(@AttrRef("id") String id, @AttrRef("name") String name, @AttrRef("phone") String phone,
			@AttrRef("address") Country address, @AttrRef("email") String email) throws ConstraintViolationException {
		super(id, name, phone, address, email);
	}
}
