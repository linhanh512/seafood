package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;

/**
 * Represents a purchaser (a subclass of Customer)
 * @author 
 *
 */
@DClass(schema = "seafoodman")
public class Purchaser extends Customer {
	// additional attribute of purchaser
	@DAttr(name = "typeOfPurchaser", type = Type.String, length = 20, optional = false)
	private String typeOfPurchaser;
	
	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	public Purchaser(@AttrRef("typeOfPurchaser") String typeOfPurchaser, @AttrRef("name") String name, 
			@AttrRef("phone") String phone, @AttrRef("address") Country address, 
			@AttrRef("email") String email) {
		this(null, name, phone, address, email, typeOfPurchaser);
	}

	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public Purchaser(String id, String name, String phone, Country address, String email, String typeOfPurchaser) 
		throws ConstraintViolationException {
		super(id, name, phone, address, email);
		this.typeOfPurchaser = typeOfPurchaser;
	}
	
	public String getTypeOfPurchaser() {
		return typeOfPurchaser;
	}
	
	public void setTypeOfPurchaser(String typeOfPurchaser) {
		this.typeOfPurchaser = typeOfPurchaser;
	}
}
