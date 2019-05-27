package model;

import java.util.ArrayList;
import java.util.Collection;

import controller.ImportSeafood;
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
	  @DAttr(name = "id", id = true, type = Type.String, auto = true, length = 6, mutable = false, optional = false)
	  private String id;
	  private static int idCounter = 0;
	  
	  @DAttr(name="name",length=20,type=Type.String,optional=false)
	  private String name;
	  
	  @DAttr(name="bill",type=Type.Domain,serialisable=false)
	  @DAssoc(ascName="bill-has-table",role="table",
	  ascType=AssocType.One2One, endType=AssocEndType.One,
	  associate=@Associate(type=SeafoodBill.class,cardMin=1,cardMax=1,determinant=true))
	  private SeafoodBill bill;
	  
	  @DAttr(name="row",type=Type.Domain,serialisable=false)
	  @DAssoc(ascName="row-has-table",role="table",
	  ascType=AssocType.One2Many,endType=AssocEndType.One,
	  associate=@Associate(type=OrderRow.class,cardMin=1,cardMax=25))  
	  private Collection<OrderRow> rows;
	  
	  @DAttr(name="RowLists",type=Type.Collection,optional = false,
			  serialisable=false,filter=@Select(clazz=OrderRow.class))
	  @DAssoc(ascName="rows-has-tab",role="tab",
		      ascType=AssocType.One2Many,endType=AssocEndType.One,
		      associate=@Associate(type=OrderRow.class,cardMin=0,cardMax=30))
	  private Collection<OrderRow> RowLists;
	  // derived attributes
	  private int OrderRowCount;
	  
	  @DOpt(type=DOpt.Type.ObjectFormConstructor)
	  @DOpt(type=DOpt.Type.RequiredConstructor)
	  public OrderTable(@AttrRef("name") String name) {
	    this(null, name,null,null);
	  }

	// from object form: Student is included
	  @DOpt(type=DOpt.Type.ObjectFormConstructor)
	  public OrderTable(@AttrRef("name") String name, @AttrRef("bill") SeafoodBill bill,@AttrRef("row")Collection<OrderRow> rows ) {
	    this(null, name, bill,rows);
	  }

	  // from data source
	  @DOpt(type=DOpt.Type.DataSourceConstructor)
	  public OrderTable(@AttrRef("id") String id, @AttrRef("bill") SeafoodBill bill) {
	    this(id, null, bill,null);
	  }
	  
	  // constructor to create objects from data source
	  @DOpt(type=DOpt.Type.DataSourceConstructor)
	  public OrderTable(@AttrRef("id") String id,@AttrRef("name") String name, SeafoodBill bill, Collection<OrderRow> row) {
	    this.id = nextID(id);
	    this.name = name;
	    this.bill = bill;
	    rows = new ArrayList<>();
	    RowLists = new ArrayList<>();
	    OrderRowCount = 0;
	    
	  }

	  @DOpt(type=DOpt.Type.Setter)
	  public void setName(String name) {
	    this.name = name;
	  }
	  
	  @DOpt(type=DOpt.Type.LinkAdderNew)
	  public void setNewBill(SeafoodBill bill) {
		  this.bill = bill;
	  }
	  
	  public void setBill(SeafoodBill bill) {
		  this.bill = bill;
	  }
	  

	  @DOpt(type=DOpt.Type.LinkRemover)
	  //@MemberRef(name="enrolments")
	  public boolean removeEnrolment(OrderRow e) {
	    boolean removed = RowLists.remove(e);
	    if (removed) {
	    	OrderRowCount--;     
	    }
	    return false; 
	  }
	  public void setEnrolments(Collection<OrderRow> en) {
		    this.RowLists = en;
		    OrderRowCount = en.size();
	  }
	  @DOpt(type = DOpt.Type.LinkAdderNew)
	  public void setNewOrderRow(Collection<OrderRow> row) {
	    this.rows = row;
	  }
	  public void setOrderRow(Collection<OrderRow> row) {
			this.rows = row;
		}
	    
	  @DOpt(type=DOpt.Type.LinkAdder)
	  //only need to do this for reflexive association: @MemberRef(name="enrolments")
	  public boolean addEnrolment(OrderRow e) {
	    if (!RowLists.contains(e))
	    	RowLists.add(e);
	    return false; 
	  }
	  
	  @DOpt(type=DOpt.Type.LinkAdderNew)
	  public boolean addNewEnrolment(OrderRow e) {
		  RowLists.add(e);
	    
		  OrderRowCount++;
	    // no other attributes changed (average mark is not serialisable!!!)
	    return false; 
	  }
	  @DOpt(type=DOpt.Type.LinkAdder)
	  //@MemberRef(name="enrolments")
	  public boolean addEnrolment(Collection<OrderRow> enrols) {
	    boolean added = false;
	    for (OrderRow e : enrols) {
	      if (!RowLists.contains(e)) {
	        if (!added) added = true;
	        RowLists.add(e);
	      }
	    }
	    return false; 
	  }
	  @DOpt(type=DOpt.Type.LinkAdderNew)
	  public boolean addNewEnrolment(Collection<OrderRow> enrols) {
		  RowLists.addAll(enrols);
		  OrderRowCount+=enrols.size();
	    return false; 
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
	  public Collection<OrderRow> getEnrolments() {
		    return RowLists;
	  }

	  @DOpt(type=DOpt.Type.Getter)
	  public String getId() {
	    return id;
	  }
	  
	  public SeafoodBill getBill() {
		  return bill;
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
		 * @effects returns <code>Customer(id,name,phone,address,email)</code>.
		 */
		public String toString(boolean full) {
			if (full)
				return "Table(" + id + "," + name +  ")";
			else
				return "Table(" + id + ")";
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
			OrderTable other = (OrderTable) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
	  }

	// automatically generate the next student id
		private String nextID(String id) throws ConstraintViolationException {
			if (id == null) {
				// generate a new id
				idCounter++;
				if (idCounter >= 10) {
					return "Ta" + idCounter;
				} else {
					return "Ta0" + idCounter;
				}
			} else {
				// update id
				int num;
				try {
					num = Integer.parseInt(id.substring(1));
				} catch (RuntimeException e) {
					throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
							new Object[] { id });
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
