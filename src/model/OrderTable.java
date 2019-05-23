package model;

import java.util.ArrayList;
import java.util.Collection;
import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;

/**
 * @overview an order table contains various of order rows
 * 
 * @author Do Thi Thuy Linh
 *
 */
@DClass(schema="seafoodman")
public class OrderTable {
	@DAttr(name="id",id=true,auto=true,length=6,mutable=false,type=Type.Integer)
	  private int id;
	  private static int idCounter;
	  
	  @DAttr(name="name",length=20,type=Type.String,optional=false)
	  private String name;
	  
	  @DAttr(name="table",type=Type.Domain,optional=false)
	  @DAssoc(ascName="bill-has-order-table",role="table",
	  ascType=AssocType.One2One, endType=AssocEndType.One,
	  associate=@Associate(type=SeafoodBill.class,cardMin=1,cardMax=1))
	  private SeafoodBill bill;
	  
	  @DAttr(name="OrderRow",type=Type.Collection,
	      serialisable=false,optional=false,
	      filter=@Select(clazz=OrderRow.class))
	  @DAssoc(ascName="Table-has-OrderRow",role="table",
	      ascType=AssocType.One2Many,endType=AssocEndType.One,
	      associate=@Associate(type=OrderRow.class,
	      cardMin=1,cardMax=25))  
	  private Collection<OrderRow> rows;
	  
	  // derived attributes
	  private int OrderRowCount;
	  
	  @DOpt(type=DOpt.Type.ObjectFormConstructor)
	  @DOpt(type=DOpt.Type.RequiredConstructor)
	  public OrderTable(@AttrRef("name") String name) {
	    this(null, name);
	  }

	  // constructor to create objects from data source
	  @DOpt(type=DOpt.Type.DataSourceConstructor)
	  public OrderTable(@AttrRef("id") Integer id,@AttrRef("name") String name) {
	    this.id = nextID(id);
	    this.name = name;
	    
	    rows = new ArrayList<>();
	    OrderRowCount = 0;
	  }

	  @DOpt(type=DOpt.Type.Setter)
	  public void setName(String name) {
	    this.name = name;
	  }
	  
	  public void setBill(SeafoodBill bill) {
		  this.bill = bill;
	  }

	  @DOpt(type=DOpt.Type.LinkAdder)
	  //only need to do this for reflexive association: @MemberRef(name="OrderRow")  
	  public boolean addOrderRow(OrderRow s) {
	    if (!this.rows.contains(s)) {
	      rows.add(s);
	    }
	    
	    // no other attributes changed
	    return false; 
	  }

	  @DOpt(type=DOpt.Type.LinkAdderNew)
	  public boolean addNewOrderRow(OrderRow s) {
	    rows.add(s);
	    OrderRowCount++;
	    
	    // no other attributes changed
	    return false; 
	  }
	  
	  @DOpt(type=DOpt.Type.LinkAdder)
	  public boolean addOrderRow(Collection<OrderRow> OrderRow) {
	    for (OrderRow s : OrderRow) {
	      if (!this.rows.contains(s)) {
	        this.rows.add(s);
	      }
	    }
	    
	    // no other attributes changed
	    return false; 
	  }

	  @DOpt(type=DOpt.Type.LinkAdderNew)
	  public boolean addNewOrderRow(Collection<OrderRow> OrderRow) {
	    this.rows.addAll(OrderRow);
	    OrderRowCount += OrderRow.size();

	    // no other attributes changed
	    return false; 
	  }

	  @DOpt(type=DOpt.Type.LinkRemover)
	  //only need to do this for reflexive association: @MemberRef(name="OrderRow")
	  public boolean removeOrderRow(OrderRow s) {
	    boolean removed = rows.remove(s);
	    
	    if (removed) {
	      OrderRowCount--;
	    }
	    
	    // no other attributes changed
	    return false; 
	  }
	  
	  @DOpt(type=DOpt.Type.Setter)
	  public void setOrderRow(Collection<OrderRow> OrderRow) {
	    this.rows = OrderRow;
	    
	    OrderRowCount = OrderRow.size();
	  }
	    
	  /**
	   * @effects 
	   *  return <tt>OrderRowCount</tt>
	   */
	  @DOpt(type=DOpt.Type.LinkCountGetter)
	  public Integer getOrderRowCount() {
	    return OrderRowCount;
	  }

	  @DOpt(type=DOpt.Type.LinkCountSetter)
	  public void setOrderRowCount(int count) {
	    OrderRowCount = count;
	  }
	  
	  @DOpt(type=DOpt.Type.Getter)
	  public String getName() {
	    return name;
	  }
	  
	  @DOpt(type=DOpt.Type.Getter)
	  public Collection<OrderRow> getOrderRow() {
	    return rows;
	  }
	  
	  @DOpt(type=DOpt.Type.Getter)
	  public int getId() {
	    return id;
	  }
	  
	  public SeafoodBill getBill() {
		  return bill;
	  }
	  
	  @Override
	  public String toString() {
	    return "OrderTable("+getId()+","+getName()+")";
	  }
	  
	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + id;
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
	    OrderTable other = (OrderTable) obj;
	    if (id != other.id)
	      return false;
	    return true;
	  }

	  private static int nextID(Integer currID) {
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
	      if (attrib.name().equals("id")) {
	        int maxIdVal = (Integer) maxVal;
	        if (maxIdVal > idCounter)  
	          idCounter = maxIdVal;
	      }
	    }
	  }
}
