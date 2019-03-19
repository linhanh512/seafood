package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;

@DClass(schema="seafoodman")
public class DomesticSeafood extends Seafood{

	@DOpt(type=DOpt.Type.DataSourceConstructor)
	public DomesticSeafood(@AttrRef("id") String id, @AttrRef("name") String name) 
			throws ConstraintViolationException {
		super(id, name);
	}
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public DomesticSeafood(@AttrRef("name") String name) {
		super(null,name);
	}

}
