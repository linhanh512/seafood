package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;
import domainapp.basics.util.cache.StateHistory;
import model.Seafood;
import model.OrderTable;

/**
 * @overview an order Rows give each row in orderList in SeafoodBill
 * 
 * @author Do Thi Thuy Linh
 *
 */
@DClass(schema="seafoodman")
public class OrderRow implements Comparable{
	
	public static final String A_id="id";
	public static final String A_seafood="seafood";
	public static final String A_number="numberInKilo";
	public static final String A_pricePerKilo="pricePerKilo";
	public static final String A_price="price";
	public static final String A_bill="bill";
	
	//attributes
	@DAttr(name=A_id,id=true,auto=true,type=Type.Integer,length=3,mutable=false,optional=false)
	private int id;
	private static int idCounter = 0;
	
	@DAttr(name=A_seafood,type=Type.Domain,optional=false,length = 6)
	@DAssoc(ascName="order-has-seafood",role="order",
	ascType=AssocType.One2One, endType=AssocEndType.One,
	associate=@Associate(type=Seafood.class,cardMin=1,cardMax=1))
	private Seafood seafood;
	
	@DAttr(name=A_number,type=Type.Double,length=4,optional=false)
	private Double numberInKilo;
	
	@DAttr(name=A_pricePerKilo,type=Type.Double,length=6,optional=false)
	private Double pricePerKilo;
	
	@DAttr(name=A_price,auto=true, type=Type.Double,length=6,optional=true, mutable=false,
			serialisable=false,derivedFrom= {A_pricePerKilo,A_number})
	private Double price;	
	
	private StateHistory<String, Object> stateHist;
	
	@DAttr(name="table",type=Type.Domain,optional=true)
	@DAssoc(ascName="rows-has-tab",role="rows",
	ascType=AssocType.One2Many, endType=AssocEndType.Many,
	associate=@Associate(type=OrderTable.class,cardMin=1,cardMax=1),dependsOn = true)
	private OrderTable table;
	
	//constructor
	@DOpt(type=DOpt.Type.DataSourceConstructor)
	public OrderRow(Integer id, Seafood seafood,Double pricePerKilo, Double numberInKilo,
			Double price, OrderTable table) throws ConstraintViolationException{
		this.id = nextId(id);
		this.seafood = seafood;
		this.table = table;
		this.pricePerKilo = (pricePerKilo != null)? pricePerKilo.doubleValue():null;
		this.numberInKilo = (numberInKilo != null)? numberInKilo.doubleValue():null;
		stateHist = new StateHistory<>();
		updateFinalPrice();
	}
	
	@DOpt(type=DOpt.Type.DataSourceConstructor)
	public OrderRow(Integer id, Seafood seafood,Double pricePerKilo, Double numberInKilo,OrderTable table) {
		this(id,seafood,pricePerKilo,numberInKilo,null,table);
	}
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	public OrderRow(Seafood seafood, Double pricePerKilo, Double numberInKilo, OrderTable table) {
		this(null,seafood,pricePerKilo,numberInKilo,null,table);
	}
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public OrderRow(Seafood seafood,OrderTable table) throws ConstraintViolationException {
		this(null,seafood,0.0,0.0,null,table);
	}
	
	//setter
	public void setSeafood(Seafood seafood) {
		this.seafood = seafood;
	}

	public void setPricePerKilo(Double price) {
		setPricePerKilo(price,false);
	}
	
	public void setTable(OrderTable table) {
		this.table = table;
	}
	
	public void setPricePerKilo(Double price,boolean updateFinalPrice) {
		this.pricePerKilo = price;
		if(updateFinalPrice)
			updateFinalPrice();
	}
	
	public void setNumberInKilo(Double number) {
		setNumberInKilo(number,false);
	}
	
	public void setNumberInKilo(Double number,boolean updateFinalPrice) {
		this.numberInKilo = number;
		if(updateFinalPrice)
			updateFinalPrice();
	}
	
	@DOpt(type=DOpt.Type.DerivedAttributeUpdater)
	@AttrRef(value=A_price)
	private void updateFinalPrice() {
		if(!numberInKilo.equals(null) && !pricePerKilo.equals(null)) {
			price = numberInKilo * pricePerKilo;
			
			stateHist.put(A_price, price);
		}
	}
	//getter
	public int getId() {
		return id;
	}
	
	public double getPricePerKilo() {
		return pricePerKilo;
	}
	
	public double getNumberInKilo() {
		return numberInKilo;
	}
	
	public double getPrice() {
		return getPrice(false);
	}
	
	public double getPrice(boolean cached) throws IllegalStateException {
		if(cached) {
			Object val = stateHist.get(A_price);
			
			if(val == null)
				throw new IllegalStateException(
						"OrderRow.getPrice: cached value is null");
			
			return (Double) val;
			
		}else {
			if(price != null)
				return price;
			else
				return 0.0;
		}
	}
	
	public Seafood getSeafood() {
		return seafood;
	}
	
	public OrderTable getTable() {
		return table;
	}
	
	private Integer nextId(Integer currID) throws ConstraintViolationException{
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
	  
	  /**
		* @effects returns <code>this.id</code>
		*/
		@Override
		public String toString() {
			return toString(true);
		}

		public String toString(boolean full) {
			if (full)
				return "Order(" + id + ", "+ seafood.toString(false) +", "+ numberInKilo +", "+price+")";
		    else
		    	return "Order(" + id + ")";
		}

		@Override
		public int compareTo(Object o) {
			if (o == null || (!(o instanceof OrderRow)))
			      return -1;

			    OrderRow e = (OrderRow) o;

			    return this.seafood.getId().compareTo(e.seafood.getId());
		}
}
