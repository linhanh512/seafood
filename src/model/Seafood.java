package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.util.Tuple;

/**
 * @overview represent a Seafood object
 * 
 * @author Do Thi Thuy Linh
 */
@DClass(schema="seafoodman")
public class Seafood {
	
	public static final String A_name = "name";
	public static final String A_id = "id";
	
	//attribute
	@DAttr(name=A_id,id=true,auto=true,type=Type.String,length=3,mutable=false,optional=false)
	private String id;
	private static int idCounter=0;
	
	@DAttr(name=A_name,type=Type.String,length=35,optional=false)
	private String name;
	
	//Constructors
	//Constructor with id and name
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public Seafood(@AttrRef("id") String id, @AttrRef("name") String name)
	throws ConstraintViolationException{
		this.id = nextID(id);
		this.name = name;
	}
	
	//Constructor with name only
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public Seafood(@AttrRef("name") String name) {
		this(null,name);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	// override toString
	/**
	* @effects returns <code>this.id</code>
	*/
	@Override
	public String toString() {
		return toString(true);
	}

	/**
	* @effects returns <code>Student(id,name,dob,address,email)</code>.
	*/
	public String toString(boolean full) {
		if (full)
			return "Seafood(" + id + "," + name + ")";
	    else
	    	return "Seafood(" + id + ")";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
	    int result = 1;
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
	    	return false;
	    if (getClass() != obj.getClass())
	    	return false;
	    Seafood other = (Seafood) obj;
	    if (id == null) {
	    	if (other.id != null)
	    		return false;
	    } else if (!id.equals(other.id))
	    	return false;
	    return true;
	  	}
	  
	private String nextID(String id) throws ConstraintViolationException {
	    if (id == null) {
	    	// generate a new id
	        idCounter++;
	        if(idCounter >=10) {
	        	return "S" + idCounter;
	        }else {
	        	return "S0" + idCounter;
	        }
		}else {
	    	// update id
	    	int num;
	    	try {
	        num = Integer.parseInt(id.substring(1));
	    	} catch (RuntimeException e) {
	    		throw new ConstraintViolationException(
	    				ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { id });
	    	}
	      
	    	if (num > idCounter) {
	        idCounter = num;
	    	}
	      
	    	return id;
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
			  String maxId = (String) maxVal;
			  try {
				  int maxIdNum = Integer.parseInt(maxId.substring(1));
	        
				  if (maxIdNum > idCounter) // extra check
					  idCounter = maxIdNum;
	        
			  } catch (RuntimeException e) {
				  throw new ConstraintViolationException(
					ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] {maxId});
			  }
	    }
	  }
}