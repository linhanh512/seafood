﻿package controller;

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
import domainapp.basics.util.Tuple;
import domainapp.basics.util.cache.StateHistory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import model.Country;
import model.Customer;
import model.Preserver;
import model.Seafood;
import model.reports.ImportSeafoodByDateReport;

/**
 * Represents an Import Seafood
 * 
 * @author Nguyen Thanh Tung
 * 
 */
@DClass(schema = "courseman")
public class ImportSeafood implements Comparable {
	
  public static final String A_Id = "id";
  public static final String A_Customer = "customer";
  public static final String A_Preserver = "preserver";
  public static final String A_Quantity = "quantity";
  public static final String A_Price = "price";
  public static final String A_Date = "date";
  public static final String A_Total = "total";
  public static final String A_rptImportSeafoodByDate = "rptImportSeafoodByDate";
  //public static final String A_rptImportSeafoodByPrice = "rptImportSeafoodByPrice";
  
  // attributes
  @DAttr(name = A_Id, id = true, auto = true, type = Type.Integer, length = 5, optional = false, mutable = false)
  private int id;
  private static int idCounter = 0;

  @DAttr(name = "seafood", type = Type.Domain, length = 5, optional = false)
  @DAssoc(ascName = "seafood-has-import", role = "import", 
    ascType = AssocType.One2Many, endType = AssocEndType.Many, 
    associate = @Associate(type = Seafood.class, cardMin = 1, cardMax = 1), dependsOn = true)
  private Seafood seafood;
  
  @DAttr(name = A_Preserver, type = Type.Domain, length = 20, optional = false)
  @DAssoc(ascName = "imSeafood-has-preser", role = "imSeafood", 
  ascType = AssocType.One2Many, endType = AssocEndType.Many, 
  associate = @Associate(type = Country.class, cardMin = 1, cardMax = 1),dependsOn = true)
  private Preserver preserver;
  
  @DAttr(name = A_Customer, type = Type.Domain, length = 5, optional = false)
  @DAssoc(ascName = "customer-has-import", role = "import", 
    ascType = AssocType.One2Many, endType = AssocEndType.Many, 
    associate = @Associate(type = Customer.class, cardMin = 1, cardMax = 1), dependsOn = true)
  private Customer customer;

  @DAttr(name = A_Quantity, type = Type.Double, length = 4, optional = false, min = 0.0)
  private Double quantity;
  
  @DAttr(name = A_Price, type = Type.Double, length = 4, optional = false, min = 0.0)
  private Double price;

  @DAttr(name = A_Date,type = Type.String,length = 20, optional = false )
  private String date;
  // v2.6.4.b derived from two attributes
  @DAttr(name = A_Total,type=Type.Double,auto=true,mutable = false,optional = true,
      serialisable=false,
      derivedFrom={A_Quantity, A_Price})
  private Double total;
  
  @DAttr(name = A_rptImportSeafoodByDate, type = Type.Domain, serialisable = false,
			// IMPORTANT: set virtual=true to exclude this attribute from the object state
			// (avoiding the view having to load this attribute's value from data source)
			virtual = true)
  private ImportSeafoodByDateReport rptImportSeafoodByDate;

  // v2.6.4.b
  private StateHistory<String, Object> stateHist;

  // constructor method
  @DOpt(type=DOpt.Type.ObjectFormConstructor)
  @DOpt(type=DOpt.Type.RequiredConstructor)
  public ImportSeafood(@AttrRef("seafood") Seafood s, 
      @AttrRef("customer") Customer m, @AttrRef("preserver") Preserver p) throws ConstraintViolationException, ParseException {
    this(null, s, m,p, 0.0, 0.0,null);
  }

  @DOpt(type=DOpt.Type.ObjectFormConstructor)
  public ImportSeafood(@AttrRef("seafood") Seafood s, 
      @AttrRef("customer") Customer m,
      @AttrRef("preserver") Preserver p,
      @AttrRef("quantity") Double quantity, 
      @AttrRef("price") Double price,
      @AttrRef("date") String date)
      throws ConstraintViolationException, ParseException {
    this(null, s, m,p, quantity, price,date);
  }

  // @version 2.0
  @DOpt(type=DOpt.Type.DataSourceConstructor)
  public ImportSeafood(Integer id, Seafood s, Customer m,Preserver p, Double quantity,
      Double price, String date) throws ConstraintViolationException, ParseException {
    this.id = nextID(id);
    this.seafood = s;
    this.customer = m;
    this.preserver = p;
    this.quantity = (quantity != null) ? quantity.doubleValue()
        : null;
    this.price = (price != null) ? price.doubleValue() : null;
    this.date = checkDate(date);
    // v2.6.4.b
    stateHist = new StateHistory<>();
    updateTotal(); 
  }
  public ImportSeafoodByDateReport getRptImportSeafoodByDate() {
	  return rptImportSeafoodByDate;
  }
  
  //public ImportSeafoodByPriceReport getRptImportSeafoodByPrice() {
	//  return rptImportSeafoodByPrice;
  //}

  private String checkDate(String date) throws ParseException  {
	  DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	  format.setLenient(false);
	  format.parse(date);
	  
	 return date;
}

// setter methods
  public void setSeafood(Seafood s) {
    this.seafood = s;
  }

  public void setCustomer(Customer m) {
    this.customer = m;
  }

  public void setPreserver(Preserver p) {
	  this.preserver = p;
  }
  public void setQuantity(Double quan) {
    this.quantity = quan;
      updateTotal(); 
  }

  public void setPrice(Double price) {
    this.price = price;
      updateTotal(); 
  }
  public void setDate(String date) {
	  this.date = date;
  }

  @DOpt(type=DOpt.Type.DerivedAttributeUpdater)
  @AttrRef(value=A_Total)
  public void updateTotal() {

    if (quantity != null && price != null) {
      double totalPrice =  quantity * price;
      
      // v2.6.4b: cache final mark
      stateHist.put(A_Total, total);

      // round the mark to the closest integer value
      total = totalPrice;
    }
  }
  
  // getter methods
  public int getId() {
    return id;
  }

  public Seafood getSeafood() {
    return seafood;
  }

  public Customer getCustomer() {
    return customer;
  }
  public Preserver getPreserver() {
	  return preserver;
  }

  public Double getQuantity() {
    return quantity;
  }

  public Double getPrice() {
    return price;
  }
  public String getDate() {
	  return date;
  }
  
  
//  public ImportSeafoodByPriceReport getRptImportSeafoodByPrice() {
//	  return rptImportSeafoodByPrice;
//  }

  // v2.6.4.b
  public Double getTotal() {
    return getTotal(false);// finalMark;
  }

  
  public Double getTotal(boolean cached) throws IllegalStateException {
    if (cached) {
      Object val = stateHist.get(A_Total);
      if (val == null)
        throw new IllegalStateException(
            "Enrolment.getFinalMark: cached value is null");
      return (Double) val;
    } else {
      if (total != null)
        return total;
      else
        return 0.0;
    }

  }

  // override toString
  @Override
  public String toString() {
    return toString(false);
  }

  public String toString(boolean full) {
    if (full)
      return "Import(" + seafood + "," + customer+"," + preserver+ ")";
    else
      return "Import(" + getId() + "," + seafood.getId() + ","
          + customer.getId() + ")";
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
    ImportSeafood other = (ImportSeafood) obj;
    if (id != other.id)
      return false;
    return true;
  }

  private static int nextID(Integer currID) {
    if (currID == null) { // generate one
      idCounter++;
      return idCounter;
    } else { // update
      int num;
      num = currID.intValue();

      // if (num <= idCounter) {
      // throw new
      // ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE,
      // "Lỗi giá trị thuộc tính ID: {0}", num + "<=" + idCounter);
      // }

      if (num > idCounter) {
        idCounter = num;
      }
      return currID;
    }
  }

  /**
   * @requires minVal != null /\ maxVal != null
   * @effects update the auto-generated value of attribute <tt>attrib</tt>,
   *          specified for <tt>derivingValue</tt>, using
   *          <tt>minVal, maxVal</tt>
   */
  @DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
  public static void updateAutoGeneratedValue(DAttr attrib,
      Tuple derivingValue, Object minVal, Object maxVal)
      throws ConstraintViolationException {
    if (minVal != null && maxVal != null) {
      // check the right attribute
      if (attrib.name().equals("id")) {
        int maxIdVal = (Integer) maxVal;
        if (maxIdVal > idCounter)
          idCounter = maxIdVal;
      }
      // TODO add support for other attributes here
    }
  }


  // implements Comparable interface
  public int compareTo(Object o) {
    if (o == null || (!(o instanceof ImportSeafood)))
      return -1;

    ImportSeafood e = (ImportSeafood) o;

    return this.seafood.getId().compareTo(e.seafood.getId());
  }
}
