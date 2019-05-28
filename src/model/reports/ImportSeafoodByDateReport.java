package model.reports;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

import controller.ImportSeafood;
import domainapp.basics.core.dodm.dsm.DSMBasic;
import domainapp.basics.core.dodm.qrm.QRM;
import domainapp.basics.exceptions.DataSourceException;
import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.model.Oid;
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
import domainapp.basics.model.meta.Select;
import domainapp.basics.model.query.Expression.Op;
import domainapp.basics.model.query.Query;
import domainapp.basics.model.query.QueryToolKit;
import domainapp.basics.modules.report.model.meta.Output;
import model.Customer;

/**
 * @overview 
 * 	Represent the reports about Customers by name.
 * 
 * @author 
 *
 * @version 5.0
 */
@DClass(schema="seafoodman",serialisable=false)
public class ImportSeafoodByDateReport {
  @DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, optional = false, mutable = false)
  private int id;
  private static int idCounter = 0;

  /**input: Import date */
  @DAttr(name = "date", type = Type.String, length = 30, optional = false)
  private String date;
  
  /**output: Import date which date match {@link #date} */
  @DAttr(name="importSeafoods",type=Type.Collection,optional=false, mutable=false,
      serialisable=false,filter=@Select(clazz=ImportSeafood.class, 
      attributes={ImportSeafood.A_Id, ImportSeafood.A_Quantity, ImportSeafood.A_Price, ImportSeafood.A_Date, 
    		  ImportSeafood.A_Total,ImportSeafood.A_Customer,ImportSeafood.A_Preserver, ImportSeafood.A_rptImportSeafoodByDate}),derivedFrom={"date"})
  @DAssoc(ascName="import-seafood-by-date-report-has-import-seafood",role="report",
      ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(type=ImportSeafood.class,cardMin=0,cardMax=MetaConstants.CARD_MORE))
  @Output
  private Collection<ImportSeafood> importSeafoods;

  /**output: number of Customers found (if any), derived from {@link #Customers} */
  @DAttr(name = "numImports", type = Type.Integer, length = 20, auto=true, mutable=false)
  @Output
  private int numImports;
  
  /**
   * @effects 
   *  initialise this with <tt>name</tt> and use {@link QRM} to retrieve from data source 
   *  all {@link Customer} whose names match <tt>name</tt>.
   *  initialise {@link #Customers} with the result if any.
   *  
   *  <p>throws NotPossibleException if failed to generate data source query; 
   *  DataSourceException if fails to read from the data source
   * 
   */
  @DOpt(type=DOpt.Type.ObjectFormConstructor)
  @DOpt(type=DOpt.Type.RequiredConstructor)
  public ImportSeafoodByDateReport(@AttrRef("date") String date) throws NotPossibleException, DataSourceException
  , ParseException {
    this.id=++idCounter;
    this.date = checkDate(date);
    doReportQuery();
  }
  
  /**
   * @effects return name
   */
  public String getDate() {
    return date;
  }

  /**
   * @effects <pre>
   *  set this.name = name
   *  if name is changed
   *    invoke {@link #doReportQuery()} to update the output attribute value
   *    throws NotPossibleException if failed to generate data source query; 
   *    DataSourceException if fails to read from the data source.
   *  </pre>
   */
  public void setDate(String date) throws NotPossibleException, DataSourceException {
	  // boolean doReportQuery = (name != null && !name.equals(this.name));
    
    this.date = date;
    
    // DONOT invoke this here if there are > 1 input attributes!
    doReportQuery();
  }
  
  private String checkDate(String date) throws ParseException  {
	  DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	  format.setLenient(false);
	  format.parse(date);
	  
	  return date;
  }


  /**
   * This method is invoked when the report input has be set by the user. 
   * 
   * @effects <pre>
   *   formulate the object query
   *   execute the query to retrieve from the data source the domain objects that satisfy it 
   *   update the output attributes accordingly.
   *  
   *  <p>throws NotPossibleException if failed to generate data source query; 
   *  DataSourceException if fails to read from the data source. </pre>
   */
  @DOpt(type=DOpt.Type.DerivedAttributeUpdater)
  @AttrRef(value="ImportSeafoods")
  public void doReportQuery() throws NotPossibleException, DataSourceException {
    // the query manager instance
    
    QRM qrm = QRM.getInstance();
    
    // create a query to look up Customer from the data source
    // and then populate the output attribute (Customers) with the result
    DSMBasic dsm = qrm.getDsm();
    
    //TODO: to conserve memory cache the query and only change the query parameter value(s)
    Query q = QueryToolKit.createSearchQuery(dsm, ImportSeafood.class, 
        new String[] {ImportSeafood.A_Date}, 
        new Op[] {Op.MATCH}, 
        new Object[] {"%"+date+"%"});
    
    Map<Oid, ImportSeafood> result = qrm.getDom().retrieveObjects(ImportSeafood.class, q);
    
    if (result != null) {
      // update the main output data 
    	importSeafoods = result.values();
      
      // update other output (if any)
    	numImports = importSeafoods.size();
    } else {
      // no data found: reset output
      resetOutput();
    }
  }

  /**
   * @effects 
   *  reset all output attributes to their initial values
   */
  private void resetOutput() {
	  importSeafoods = null;
	  numImports = 0;
  }

  /**
   * A link-adder method for {@link #Customers}, required for the object form to function.
   * However, this method is empty because Customers have already be recorded in the attribute {@link #Customers}.
   */
  @DOpt(type=DOpt.Type.LinkAdder)
  public boolean addImportSeafood(Collection<ImportSeafood> importSeafoods) {
    // do nothing
    return false;
  }
  
  /**
   * @effects return Customers
   */
  public Collection<ImportSeafood> getImportSeafoods() {
    return importSeafoods;
  }
  
  /**
   * @effects return numImportSeafoods
   */
  public int getNumImports() {
    return numImports;
  }

  /**
   * @effects return id
   */
  public int getId() {
    return id;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ImportSeafoodByDateReport other = (ImportSeafoodByDateReport) obj;
    if (id != other.id)
      return false;
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public String toString() {
    return "ImportSeafoodByDateReport (" + id + ", " + date + ")";
  }

}
