package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.MetaConstants;
import domainapp.basics.util.Tuple;
import model.Country;
import model.reports.CustomerByNameReport;

/**
 * Represents a customer. The customer ID is auto-incremented
 * 
 * @author Nguyen Thanh Tung
 */
@DClass(schema = "seafoodman")
public abstract class Customer {
	public static final String A_name = "name";
	public static final String A_id = "id";
	public static final String A_phone = "phone";
	public static final String A_address = "address";
	public static final String A_email = "email";
	public static final String A_rptCustomerByName = "rptCustomerByName";
	public static final String A_bill = "bill";

	// attributes of customers
	@DAttr(name = A_id, id = true, type = Type.String, auto = true, length = 6, mutable = false, optional = false)
	private String id;
	// static variable to keep track of customer id
	private static int idCounter = 0;

	@DAttr(name = A_name, type = Type.String, length = 30, optional = false)
	private String name;

	@DAttr(name = A_phone, type = Type.String, length = 15, optional = false)
	private String phone;

	@DAttr(name = A_address, type = Type.Domain, length = 20, optional = true)
	@DAssoc(ascName = "customer-has-country", role = "customer", ascType = AssocType.One2One, endType = AssocEndType.One, associate = @Associate(type = Country.class, cardMin = 1, cardMax = 1))
	private Country address;

	@DAttr(name = A_email, type = Type.String, length = 30, optional = false)
	private String email;
	
	@DAttr(name=A_bill,type=Type.Domain,serialisable=false)
	@DAssoc(ascName="bill-has-customer",role="customer",
	ascType=AssocType.One2Many, endType=AssocEndType.Many,
	associate=@Associate(type=Seafood.class,cardMin=1,cardMax=MetaConstants.CARD_MORE))
	private Collection<SeafoodBill> bills;

	@DAttr(name = A_rptCustomerByName, type = Type.Domain, serialisable = false,
			// IMPORTANT: set virtual=true to exclude this attribute from the object state
			// (avoiding the view having to load this attribute's value from data source)
			virtual = true)
	private CustomerByNameReport rptCustomerByName;

	// constructor methods
	@DOpt(type = DOpt.Type.RequiredConstructor)
	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	public Customer(@AttrRef("name") String name, @AttrRef("phone") String phone, @AttrRef("address") Country address,
			@AttrRef("email") String email) {
		this(null, name, phone, address, email,null);
	}

	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	public Customer(@AttrRef("name") String name, @AttrRef("phone") String phone, @AttrRef("address") Country address,
			@AttrRef("email") String email, @AttrRef("bill") Collection<SeafoodBill> bills) {
		this(null, name, phone, address, email,bills);
	}
	
	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	public Customer(@AttrRef("id") String id,@AttrRef("name") String name, @AttrRef("phone") String phone, @AttrRef("address") Country address,
			@AttrRef("email") String email) {
		this(id, name, phone, address, email,null);
	}
	
	// a shared constructor that is invoked by other constructors
	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public Customer(@AttrRef("id") String id, @AttrRef("name") String name, @AttrRef("phone") String phone,
			@AttrRef("address") Country address, @AttrRef("email") String email, 
			@AttrRef("bill") Collection<SeafoodBill> bills) throws ConstraintViolationException {
		// generate an id
		this.id = nextID(id);
		// assign other values
			this.name = name;
			this.phone = phone;
			this.address = address;
			this.email = email;
			this.bills = new ArrayList<>();
			
	}

	public CustomerByNameReport getRptCustomerByName() {
		return rptCustomerByName;
	}

	// setter methods
	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(Country address) {
		this.address = address;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public void setBill(Collection<SeafoodBill> bill) {
		this.bills = bill;
	}


	// getter methods
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public Country getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	
	public Collection<SeafoodBill> getBill(){
		return bills;
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
			return "Customer(" + id + "," + name + "," + phone + "," + address + "," + email + ")";
		else
			return "Customer(" + id + ")";
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
		Customer other = (Customer) obj;
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
				return "C" + idCounter;
			} else {
				return "C0" + idCounter;
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
	 * @requires minVal != null /\ maxVal != null
	 * @effects update the auto-generated value of attribute <tt>attrib</tt>,
	 *          specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
	 */
	@DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
	public static void updateAutoGeneratedValue(DAttr attrib, Tuple derivingValue, Object minVal, Object maxVal)
			throws ConstraintViolationException {

		if (minVal != null && maxVal != null) {
			String maxId = (String) maxVal;

			try {
				int maxIdNum = Integer.parseInt(maxId.substring(1));

				if (maxIdNum > idCounter) // extra check
					idCounter = maxIdNum;

			} catch (RuntimeException e) {
				throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
						new Object[] { maxId });
			}
		}
	}
}
