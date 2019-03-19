package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.util.Tuple;

@DClass(schema="seafoodman")
public class Country {
	
	//attributes
	@DAttr(name="id",id=true,auto=true,type=Type.Integer,length=3,mutable=false,optional=false)
	private Integer id;
	private static int idCounter = 0;
	
	@DAttr(name="name",type=Type.String,length=35,optional=false)
	private String name;
	
	@DOpt(type=DOpt.Type.DataSourceConstructor)
	public Country(@AttrRef("id") Integer id, @AttrRef("name") String name) {
		this.id = nextId(id);
		this.name = name;
	}
	
	//Constructor with name only
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public Country(@AttrRef("name") String name) {
		this(null,name);
	}
	
	private static int nextId(Integer currID) {
		if (currID == null) {
			idCounter++;
			return idCounter;
	    } else {
	    	int num = currID.intValue();
	    	if (num > idCounter)
	    		idCounter = num;
	      
	    	return currID;
	    }
	 }
	
	/**
	* @requires 
	*  minVal != null /\ maxVal != null
	* @effects 
	*  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
	*/
	@DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
	public static void updateAutoGeneratedValue(
		DAttr attrib,
	    Tuple derivingValue, 
	    Object minVal, 
	    Object maxVal) throws ConstraintViolationException {
	    
	    if (minVal != null && maxVal != null) {
	    	int maxIdVal = (Integer) maxVal;
	    	if (maxIdVal > idCounter)  
	    		idCounter = maxIdVal;
	    }
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
