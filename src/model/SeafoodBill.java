package model;

import java.util.Collection;
import controller.OrderSeafood;
import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;
import model.Customer;
import model.Seafood;

/**
 * @overview represent a bill of Seafood order
 * 
 * @author Do Thi Thuy Linh
 */
public class SeafoodBill {
	
	private static final String A_id = "id";
	private static final String A_customer = "customer";
	private static final String A_seafood = "seafood";
	private static final String A_order = "order";
	
	//attributes
	@DAttr(name=A_id,id=true,auto=true,type=Type.String,length=3,mutable=false,optional=false)
	private String id;
	private static int idCounter=0;
	
	@DAttr(name=A_customer,type=Type.Domain,length=6,optional=false)
	@DAssoc(ascName="bill-has-customer",role="bill",
	ascType=AssocType.One2One, endType=AssocEndType.One,
	associate=@Associate(type=Customer.class,cardMin=1,cardMax=1))
	private Customer customer;
	
	@DAttr(name=A_seafood,type=Type.Collection,serialisable=false,optional=false,
	filter=@Select(clazz=Seafood.class))
	@DAssoc(ascName="bill-has-seafood",role="bill",
	ascType=AssocType.One2Many,endType=AssocEndType.One,
	associate=@Associate(type=Seafood.class,
	cardMin=1,cardMax=25))  
	private Collection<Seafood> seafoods;
	
	// derived attributes
	private int seafoodCount;
	
	@DAttr(name=A_order,type=Type.Domain,length=6,optional=false)
	@DAssoc(ascName="bill-has-order",role="bill",
	ascType=AssocType.One2One, endType=AssocEndType.One,
	associate=@Associate(type=OrderSeafood.class,cardMin=1,cardMax=1))
	private OrderSeafood order;
	
	//Constructors
	//Constructor with id
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public SeafoodBill(@AttrRef("id") String id,
			@AttrRef("customer") Customer customer,
			@AttrRef("seafood") Collection<Seafood> seafoods,
			@AttrRef("order") OrderSeafood order) {
		this.id = nextID(id);
		this.customer = customer;
	}
	
	//Constructor without id
	public SeafoodBill(Customer customer, Collection<Seafood> seafoods, OrderSeafood order) {
		this(null,customer,seafoods,order);
	}
	
	//setter methods
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public void setSeafoods(Collection<Seafood> seafoods) {
		this.seafoods = seafoods;
	}
	
	public void setOrder(OrderSeafood order) {
		this.order = order;
	}
	
	//getter methods
	public String getId() {
		return id;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public OrderSeafood getOrder() {
		return order;
	}
	
	public int getSeafoodCount() {
		return seafoodCount;
	}

	public void setSeafoodCount(int seafoodCount) {
		this.seafoodCount = seafoodCount;
	}
	
	@DOpt(type=DOpt.Type.LinkAdder)
	//only need to do this for reflexive association: @MemberRef(name="seafoods")  
	public boolean addSeafood(Seafood s) {
	    if (!this.seafoods.contains(s)) {
	    	seafoods.add(s);
	    }
	    
	    // no other attributes changed
	    return false; 
	}

	@DOpt(type=DOpt.Type.LinkAdderNew)
	public boolean addNewSeafood(Seafood s) {
	    seafoods.add(s);
	    setSeafoodCount(getSeafoodCount() + 1);
	    
	    // no other attributes changed
	    return false; 
	}
	
	@DOpt(type=DOpt.Type.LinkAdder)
	public boolean addSeafood(Collection<Seafood> seafoods) {
	    for (Seafood s : seafoods) {
	      if (!this.seafoods.contains(s)) {
	        this.seafoods.add(s);
	      }
	    }
	    
	    // no other attributes changed
	    return false; 
	}

	@DOpt(type=DOpt.Type.LinkAdderNew)
	public boolean addNewSeafood(Collection<Seafood> seafoods) {
	    this.seafoods.addAll(seafoods);
	    setSeafoodCount(getSeafoodCount() + seafoods.size());

	    // no other attributes changed
	    return false; 
	}

	@DOpt(type=DOpt.Type.LinkRemover)
	//only need to do this for reflexive association: @MemberRef(name="seafoods")
	public boolean removeSeafood(Seafood s) {
	    boolean removed = seafoods.remove(s);
	    
	    if (removed) {
	      setSeafoodCount(getSeafoodCount() - 1);
	    }
	    
	    // no other attributes changed
	    return false; 
	}
	
	@Override
	public String toString() {
	    return "SeafoodBill("+getId()+")";
	}
	  
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + Integer.parseInt(id.substring(1));
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
	    SeafoodBill other = (SeafoodBill) obj;
	    if (id != other.id)
	      return false;
	    return true;
	}
	  
	private String nextID(String id) throws ConstraintViolationException {
	    if (id == null) {
	    	// generate a new id
	        idCounter++;
	        if(idCounter >=10) {
	        	return "B" + idCounter;
	        }else {
	        	return "B0" + idCounter;
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
	      if (attrib.name().equals("id")) {
	        int maxIdVal = (Integer) maxVal;
	        if (maxIdVal > idCounter)  
	          idCounter = maxIdVal;
	      }
	    }
	}
}
