package model;

import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import java.util.Collection;
import controller.ExportSeafood;

@DClass(schema="seafoodman")
public class Purchaser extends Customer{
	
	@DAttr(name="exports",type=Type.Collection,mutable=true,optional=false)
	private Collection<ExportSeafood> exports;
	
	

}
