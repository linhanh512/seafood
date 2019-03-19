package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;

@DClass(schema="seafoodman")
public class ForeignSeafood extends Seafood{
	
	@DAttr(name="name",type=Type.Domain,length=35,optional=false)
	@DAssoc(ascName="foreign-seafood-has-country",role="country",
	ascType=AssocType.One2One, endType=AssocEndType.One,
	associate=@Associate(type=Country.class,cardMin=1,cardMax=1,determinant=true))
	private Country country;

	@DOpt(type=DOpt.Type.DataSourceConstructor)
	public ForeignSeafood(@AttrRef("id") String id, @AttrRef("name") String name, 
			@AttrRef("country") Country country) throws ConstraintViolationException {
		super(id, name);
		this.country = country;
	}
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public ForeignSeafood(@AttrRef("name") String name, @AttrRef("country") Country country) {
		this(null,name,country);
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	public Country getCountry() {
		return country;
	}

}
