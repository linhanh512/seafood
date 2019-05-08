package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;

/**
 * @overview represent a domestic seafood object (a subclass of Seafood)
 * 
 * @author Do Thi Thuy Linh
 */
@DClass(schema="seafoodman")
public class DomesticSeafood extends Seafood{

	@DOpt(type=DOpt.Type.DataSourceConstructor)
	public DomesticSeafood(@AttrRef("id") String id, @AttrRef("name") String name, TypeOfSeafood type) 
			throws ConstraintViolationException {
		super(id, name, type);
	}
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public DomesticSeafood(@AttrRef("name") String name,@AttrRef("type") TypeOfSeafood type) {
		super(null,name,type);
	}

	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public DomesticSeafood(@AttrRef("name") String name,@AttrRef("type") TypeOfSeafood type, OrderRow order) {
		super(name,type,order);
	}
}