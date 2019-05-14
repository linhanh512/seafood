package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.util.Tuple;
import model.reports.SeafoodByNameReport;
import model.OrderRow;

/**
 * @overview represent a Seafood object
 * 
 * @author Do Thi Thuy Linh
 */
@DClass(schema="seafoodman")
public abstract class Seafood {
	
	public static final String A_name = "name";
	public static final String A_id = "id";
	public static final String A_type = "type";
	public static final String A_rptSeafoodByName = "rptSeafoodByName";
	
	//attribute
	@DAttr(name=A_id,id=true,auto=true,type=Type.String,length=3,mutable=false,optional=false)
	private String id;
	private static int idCounter=0;
	
	@DAttr(name=A_name,type=Type.String,length=20,optional=false)
	private String name;
	
	@DAttr(name=A_type,type=Type.Domain, length = 15, optional =true)
	@DAssoc(ascName="seafood-has-type",role="seafood",
	ascType=AssocType.One2One, endType = AssocEndType.One,
	associate = @Associate(type=TypeOfSeafood.class,cardMin=1, cardMax=1))
	private TypeOfSeafood type;
	
	@DAttr(name = A_rptSeafoodByName, type = Type.Domain, serialisable = false,
			// IMPORTANT: set virtual=true to exclude this attribute from the object state
			// (avoiding the view having to load this attribute's value from data source)
			virtual = true)
	private SeafoodByNameReport rptSeafoodByName;
	
	@DAttr(name="order",type=Type.Domain,length=6)
	@DAssoc(ascName="order-has-seafood",role="seafood",
	ascType=AssocType.One2One, endType=AssocEndType.One,
	associate=@Associate(type=OrderRow.class,cardMin=1,cardMax=1))
	private OrderRow order;
	
	//Constructor without id, order
	@DOpt(type=DOpt.Type.RequiredConstructor)
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public Seafood(@AttrRef("name") String name, @AttrRef("type") TypeOfSeafood type) {
		this(null,name,type,null);
	}
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public Seafood(@AttrRef("name") String name, @AttrRef("type") TypeOfSeafood type,
			@AttrRef("order") OrderRow order) {
		this(null,name,type,order);
	}
	
	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public Seafood(@AttrRef("id") String id, @AttrRef("name") String name, @AttrRef("type") TypeOfSeafood type) {
		this(id,name,type,null);
	}
	
	private Seafood(@AttrRef("id") String id, @AttrRef("name") String name, 
			@AttrRef("type") TypeOfSeafood type,@AttrRef("order") OrderRow order)
			throws ConstraintViolationException{
		this.id = nextID(id);
		this.name = name;
		this.type = type;
		this.order = order;
	}
	
	public SeafoodByNameReport getRptSeafoodByName() {
		return rptSeafoodByName;
	}
	
	//setter
	public void setName(String name) {
		this.name = name;
	}

	public void setType(TypeOfSeafood type) {
		this.type = type;
	}
	
	public void setOrder(OrderRow order) {
		this.order = order;
	}
	
	//getter
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public TypeOfSeafood getType() {
		return type;
	}
	
	public OrderRow getOrder() {
		return order;
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
			return "Seafood(" + id + ", "+ name + ")";
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