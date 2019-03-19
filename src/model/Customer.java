package model;

import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DAttr.Type;

@DClass(schema="seafoodman")
public class Customer {
	
	//attributes
	@DAttr(name="id",id=true,auto=true,type=Type.String,length=3,mutable=false,optional=false)
	private String id;
	private static int idCounter=0;
	
	@DAttr(name="name",type=Type.String,length=35,optional=false)
	private String name;
	
	@DAttr(name="totalspent",type=Type.Integer,optional=true)
	private Integer totalSpent;

	//constructors
}
