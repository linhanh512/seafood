package model;

import java.util.Collection;
import controller.ImportSeafood;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;

@DClass(schema="seafoodman")
public class Seller extends Customer{
	
	@DAttr(name="imports",type=Type.Collection,mutable=true,optional=false)
	private Collection<ImportSeafood> Imports;
	
}
