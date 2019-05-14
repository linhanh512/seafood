package model.reports;

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
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.MetaConstants;
import domainapp.basics.model.meta.Select;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.query.Query;
import domainapp.basics.model.query.QueryToolKit;
import domainapp.basics.model.query.Expression.Op;
import domainapp.basics.modules.report.model.meta.Output; 

/**
 * @overview represent a report about imported seafood price
 *
 * @author 
 */
@DClass(schema = "seafoodman")
public class ImportSeafoodByPriceReport {
	@DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 4, optional = false, mutable = false)
	private int id;
	private static int idCounter = 0;
	
	/**input: imported seafood price*/
	@DAttr(name = "price", type = Type.Double, length = 4, optional = false)
	private double price;
	
	//output: Exported seafood whose cost match
	@DAttr(name="importseafoods",type=Type.Collection,optional=false, mutable=false,
			serialisable=false,filter=@Select(clazz=ImportSeafood.class, 
			attributes={ImportSeafood.A_Quantity, ImportSeafood.A_Price, ImportSeafood.A_Date, ImportSeafood.A_Total}),derivedFrom={"price"})
			@DAssoc(ascName="ImportSeafood-by-price-report-has-ImportSeafood",role="report",
			ascType=AssocType.One2Many,endType=AssocEndType.One,
			associate=@Associate(type=ImportSeafood.class,cardMin=0,cardMax=MetaConstants.CARD_MORE))
			@Output
			private Collection<ImportSeafood> importseafoods;
	
	
	//output: number of matched costs
	@DAttr(name = "numPrices", type = Type.Integer, length = 20, auto=true, mutable=false)
	@Output
	private int numPrices;
	/**
	* @effects 
	*  initialize this with <tt>costs</tt> and use {@link QRM} to retrieve from data source 
	*  all {@link ImportSeafood} whose costs match <tt>costs</tt>.
	*  initialise {@link #ImportSeafoods} with the result if any.
	*  
	*  <p>throws NotPossibleException if failed to generate data source query; 
	*  DataSourceException if fails to read from the data source
	* 
	*/
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public ImportSeafoodByPriceReport (@AttrRef("price") double price) throws NotPossibleException, DataSourceException {
		this.id = ++idCounter;
		this.price = price;
		doReportQuery();
	}
	
	/**
	* @effects return price
	*/
	public double getPrice() {
		return price;
	}
	
	/**
	* @effects <pre>
	*  set this.price = price
	*  if price is changed
	*    invoke {@link #doReportQuery()} to update the output attribute value
	*    throws NotPossibleException if failed to generate data source query; 
	*    DataSourceException if fails to read from the data source.
	*  </pre>
	*/
	public void setPrice(double price) throws NotPossibleException, DataSourceException {
		this.price = price;
	    doReportQuery();
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
	public void doReportQuery() throws NotPossibleException, DataSourceException  {
		QRM qrm = QRM.getInstance();
		
		// create a query to look up ImportSeafood from the data source
	    // and then populate the output attribute (ImportSeafoods) with the result
		DSMBasic dsm = qrm.getDsm();
		
		//TODO: to conserve memory cache the query and only change the query parameter value(s)
		Query q = QueryToolKit.createSearchQuery(dsm, ImportSeafood.class, 
		        new String[] {ImportSeafood.A_Price}, 
		        new Op[] {Op.MATCH}, 
		        new Object[] {"%"+price+"%"});
		
		Map<Oid, ImportSeafood> result = qrm.getDom().retrieveObjects(ImportSeafood.class, q);
		
		if (result != null) {
		      // update the main output data 
		      importseafoods = result.values();
		      
		   // update other output (if any)
		      numPrices = importseafoods.size();
		}else{
		   // no data found: reset output
		      resetOutput();
		}
	}
	
	/**
	   * @effects 
	   *  reset all output attributes to their initial values
	   */
	private void resetOutput() {
		importseafoods = null;
		numPrices = 0;
	}
	
	@DOpt(type=DOpt.Type.LinkAdder)
	public boolean addImportSeafood(Collection<ImportSeafood> importseafoods) {
		return false;	
	}
	
	/**
	   * @effects return importseafoods
	   */
	public Collection<ImportSeafood> getImportSeafoods() {
		return importseafoods;
	}
	
	/**
	   * @effects return numPrices
	   */
	public int getnumPrices() {
		return numPrices;
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
		return result = prime * result + id;	
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
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if (getClass() != obj.getClass())
		      return false;
		ImportSeafoodByPriceReport other = (ImportSeafoodByPriceReport) obj;
		if(id != other.id)
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
		return "ImportSeafoodByPrice (" + id + ", " + price +")";	
	}
}
