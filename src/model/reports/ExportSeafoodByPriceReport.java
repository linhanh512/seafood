package model.reports;
import java.util.Collection;
import java.util.Map;

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
import controller.ExportSeafood;

/**
 * @overview represent a report about exported seafood cost
 *
 * @author 
 */
@DClass(schema="seafoodman",serialisable=false)
public class ExportSeafoodByPriceReport {
	@DAttr(name = "id",id = true, auto = true, type = Type.Integer, length = 5, optional = false, mutable = false)
	private int id;
	private static int idCounter = 0;
	
	/**input: exported seafood price*/
	@DAttr(name = "price", type = Type.Double, length = 4, optional = false)
	private double price;
	
	//output: Exported seafood whose cost match
	@DAttr(name="exportseafoods",type=Type.Collection,optional=false, mutable=false,
			serialisable=false,filter=@Select(clazz=ExportSeafood.class, 
			attributes={ExportSeafood.A_Quantity, ExportSeafood.A_Price, ExportSeafood.A_Total}),derivedFrom={"price"})
			@DAssoc(ascName="ExportSeafood-by-price-report-has-ExportSeafood",role="report",
			ascType=AssocType.One2Many,endType=AssocEndType.One,
			associate=@Associate(type=ExportSeafood.class,cardMin=0,cardMax=MetaConstants.CARD_MORE))
			@Output
			private Collection<ExportSeafood> exportseafoods;
	
	//output: number of matched costs
	@DAttr(name = "numPrices", type = Type.Integer, length = 20, auto=true, mutable=false)
	@Output
	private int numPrices;
		
	/**
	* @effects 
	*  initialize this with <tt>costs</tt> and use {@link QRM} to retrieve from data source 
	*  all {@link ExportSeafood} whose costs match <tt>costs</tt>.
	*  initialise {@link #ExportSeafoods} with the result if any.
	*  
	*  <p>throws NotPossibleException if failed to generate data source query; 
	*  DataSourceException if fails to read from the data source
	* 
	*/
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public ExportSeafoodByPriceReport(@AttrRef("price") double price) throws NotPossibleException, DataSourceException {
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
	@AttrRef(value="ExportSeafoods")
	public void doReportQuery() throws NotPossibleException, DataSourceException  {
		QRM qrm = QRM.getInstance();
		
		// create a query to look up ExportSeafood from the data source
	    // and then populate the output attribute (ExportSeafoods) with the result
		DSMBasic dsm = qrm.getDsm();
		
		//TODO: to conserve memory cache the query and only change the query parameter value(s)
		Query q = QueryToolKit.createSearchQuery(dsm, ExportSeafood.class, 
		        new String[] {ExportSeafood.A_Price}, 
		        new Op[] {Op.MATCH}, 
		        new Object[] {"%"+price+"%"});
		
		Map<Oid, ExportSeafood> result = qrm.getDom().retrieveObjects(ExportSeafood.class, q);
		
		if (result != null) {
		      // update the main output data 
		      exportseafoods = result.values();
		      
		   // update other output (if any)
		      numPrices = exportseafoods.size();
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
	    exportseafoods = null;
	    numPrices = 0;
	  }
	  
	  @DOpt(type=DOpt.Type.LinkAdder)
	  public boolean addExportSeafood(Collection<ExportSeafood> exportseafoods) {
	    // do nothing
	    return false;
	  }
	  
	  /**
	   * @effects return exportseafoods
	   */
	  public Collection<ExportSeafood> getExportSeafoods() {
	    return exportseafoods;
	  }
	  
	  /**
	   * @effects return numPrices
	   */
	  public int getNumPrices() {
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
	    ExportSeafoodByPriceReport other = (ExportSeafoodByPriceReport) obj;
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
	    return "ExportSeafoodByPriceReport (" + id + ", " + price + ")";
	  }
}