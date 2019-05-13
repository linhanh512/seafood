<<<<<<< HEAD
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
	@DAttr(name = Customer.A_typeOfPurchaser , type = Type.String, length = 20, optional = false)
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
=======
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
>>>>>>> f4bc30880c4108701128c306037cc40115e9e35d
