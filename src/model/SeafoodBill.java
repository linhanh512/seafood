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

/**
 * @overview
 * a bill includes customer, order table
 * @author Do Thi Thuy Linh
 *
 */
@DClass(schema = "seafoodman")
public class SeafoodBill {

	public static final String A_billNo = "billNo";
	public static final String A_name = "name";
	public static final String A_table = "table";
	public static final String A_customer = "customer";

	// attributes
	@DAttr(name = A_billNo, id = true, auto = true, type = Type.String, length = 4, mutable = false, optional = false)
	private String billNo;
	private static int idCounter = 0;

	@DAttr(name = A_name, type = Type.String, length = 20, optional = false)
	private String name;

	@DAttr(name = "customer", type = Type.Domain, optional = false)
	@DAssoc(ascName = "bill-has-customer", role = "bill", 
	ascType = AssocType.One2One, endType = AssocEndType.One, 
	associate = @Associate(type = Customer.class, cardMin = 1, cardMax = 1))
	private Customer customer;
	
	@DAttr(name = "table", type = Type.Domain, optional = false)
	@DAssoc(ascName = "bill-has-table", role = "bill", 
	ascType = AssocType.One2One, endType = AssocEndType.One, 
	associate = @Associate(type = OrderTable.class, cardMin = 1, cardMax = 1))
	private OrderTable table;
	
//	@DAttr(name="note",type  = Type.String,optional = true)
//	private String note;

	//Constructor without id, order
		@DOpt(type=DOpt.Type.RequiredConstructor)
		@DOpt(type=DOpt.Type.ObjectFormConstructor)
		public SeafoodBill(@AttrRef(A_name) String name, @AttrRef("customer") Customer customer) {
			this(null,name,customer,null);
		}
		
		@DOpt(type=DOpt.Type.ObjectFormConstructor)
		public SeafoodBill(@AttrRef(A_name) String name, @AttrRef("customer") Customer customer,
				@AttrRef("table") OrderTable table) {
			this(null,name,customer,table);
		}
		
		@DOpt(type = DOpt.Type.DataSourceConstructor)
		public SeafoodBill(@AttrRef("id") String id, @AttrRef(A_name) String name, @AttrRef("customer") Customer customer) {
			this(id,name,customer,null);
		}
		
		public SeafoodBill(@AttrRef(A_billNo) String id, @AttrRef(A_name) String name, 
				@AttrRef("customer") Customer customer,@AttrRef("table") OrderTable table)
				throws ConstraintViolationException{
			this.billNo = nextID(id);
			this.name = name;
			this.customer = customer;
			this.table = table;
		}

	// getter
	public String getBillNo() {
		return billNo;
	}

	public String getName() {
		return name;
	}

	public Customer getCustomer() {
		return customer;
	}

	public OrderTable getTable() {
		return table;
	}
//	public String getNote() {
//		return note;
//	}

	// setter
	public void setName(String name) {
		this.name = name;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setTable(OrderTable table) {
		this.table = table;
	}
//	public void setNote(String note) {
//		this.note = note;
//	}
	/**
	 * @effects returns <code>this.billNo</code>
	 */
	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean full) {
		if (full)
			return "SeafoodBill(" + billNo + ", " + name + ")";
		else
			return "SeafoodBill(" + billNo + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((billNo == null) ? 0 : billNo.hashCode());
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
		if (billNo == null) {
			if (other.billNo != null)
				return false;
		} else if (!billNo.equals(other.billNo))
			return false;
		return true;
	}

	private String nextID(String billNo) throws ConstraintViolationException {
		if (billNo == null) {
			// generate a new billNo
			idCounter++;
			if (idCounter >= 10) {
				return "B" + idCounter;
			} else {
				return "B0" + idCounter;
			}
		} else {
			// update billNo
			int num;
			try {
				num = Integer.parseInt(billNo.substring(1));
			} catch (RuntimeException e) {
				throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
						new Object[] { billNo });
			}

			if (num > idCounter) {
				idCounter = num;
			}

			return billNo;
		}
	}
}