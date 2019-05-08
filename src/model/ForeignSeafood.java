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
			TypeOfSeafood type, Country country) {
		super(id, name,type);
		this.country = country;
	}
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public ForeignSeafood(@AttrRef("name") String name,@AttrRef("country") Country country,
			@AttrRef("type") TypeOfSeafood type ) {
		this(null,name, type,country);
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	public Country getCountry() {
		return country;
	}

}