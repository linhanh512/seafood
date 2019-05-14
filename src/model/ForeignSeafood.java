package model;

import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;

/**
 * @overview represent a foreign seafood object (a subclass of Seafood)
 * 
 * @author Do Thi Thuy Linh
 */
@DClass(schema="seafoodman")
public class ForeignSeafood extends Seafood{
	
	@DAttr(name="country",type=Type.Domain,length=6,optional=false)
	@DAssoc(ascName="foreign-seafood-has-country",role="country",
	ascType=AssocType.One2One, endType=AssocEndType.One,
	associate=@Associate(type=Country.class,cardMin=1,cardMax=1))
	private Country country;

	@DOpt(type=DOpt.Type.DataSourceConstructor)
	public ForeignSeafood(String id, String name, 
			TypeOfSeafood type, OrderRow order, Country country) {
		super(id, name,type, order);
		this.country = country;
	}
	
	//without id
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public ForeignSeafood(@AttrRef("name") String name,@AttrRef("type") TypeOfSeafood type,
			@AttrRef("order") OrderRow order, @AttrRef("country") Country country) {
		this(null,name, type,order,country);
	}
	
	//without order
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public ForeignSeafood(@AttrRef("id") String id, @AttrRef("name") String name,@AttrRef("type") TypeOfSeafood type,
			@AttrRef("country") Country country) {
		this(id,name, type, null,country);
	}
	
	//without order and id
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public ForeignSeafood(@AttrRef("name") String name,@AttrRef("type") TypeOfSeafood type,
			@AttrRef("country") Country country) {
		this(null,name, type, null,country);
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	public Country getCountry() {
		return country;
	}

}