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
import model.Seafood;

/**
 * @overview represent a report about seafood name
 * 
 * @author Do Thi Thuy Linh
 */

@DClass(schema="seafoodman",serialisable=false)
public class SeafoodByNameReport {
	
	@DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, optional = false, mutable = false)
	private int id;
	private static int idCounter = 0;
	
	//input: seafood name
	@DAttr(name = "name", type = Type.String, length = 35, optional = false)
	private String name;
	
	//output: seafood whose names match
	@DAttr(name="Seafoods",type=Type.Collection,optional=false, mutable=false,
	serialisable=false,filter=@Select(clazz=Seafood.class, 
	attributes={Seafood.A_id, Seafood.A_name}),derivedFrom={"name"})
	@DAssoc(ascName="seafood-by-name-report-has-seafood",role="seafood-by-name-report",
	ascType=AssocType.One2Many,endType=AssocEndType.One,
	associate=@Associate(type=Seafood.class,cardMin=0,cardMax=MetaConstants.CARD_MORE))
	@Output
	private Collection<Seafood> seafoods;
	
	//output: number of match seafoods
	@DAttr(name = "numSeafoods", type = Type.Integer, length = 20, auto=true, mutable=false)
	@Output
	private int numSeafoods;
	
	/**
	* @effects 
	*  initialise this with <tt>name</tt> and use {@link QRM} to retrieve from data source 
	*  all {@link Seafood} whose names match <tt>name</tt>.
	*  initialise {@link #Seafoods} with the result if any.
	*  
	*  <p>throws NotPossibleException if failed to generate data source query; 
	*  DataSourceException if fails to read from the data source
	* 
	*/
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	public SeafoodByNameReport(@AttrRef("name") String name) throws NotPossibleException, DataSourceException {
		this.id=++idCounter;
	    this.name = name;
	    doReportQuery();
	}
	  
	/**
	* @effects return name
	*/
	public String getName() {
		return name;
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
	public void setName(String name) throws NotPossibleException, DataSourceException {
		this.name = name;
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
	@AttrRef(value="Seafoods")
	public void doReportQuery() throws NotPossibleException, DataSourceException {
		QRM qrm = QRM.getInstance();
	    
	    // create a query to look up Seafood from the data source
	    // and then populate the output attribute (Seafoods) with the result
	    DSMBasic dsm = qrm.getDsm();
	    
	    //TODO: to conserve memory cache the query and only change the query parameter value(s)
	    Query q = QueryToolKit.createSearchQuery(dsm, Seafood.class, 
	        new String[] {Seafood.A_name}, 
	        new Op[] {Op.MATCH}, 
	        new Object[] {"%"+name+"%"});
	    
	    Map<Oid, Seafood> result = qrm.getDom().retrieveObjects(Seafood.class, q);
	    
	    if (result != null) {
	      // update the main output data 
	      seafoods = result.values();
	      
	      // update other output (if any)
	      numSeafoods = seafoods.size();
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
	    seafoods = null;
	    numSeafoods = 0;
	  }

	  /**
	   * A link-adder method for {@link #Seafoods}, required for the object form to function.
	   * However, this method is empty because Seafoods have already be recorded in the attribute {@link #Seafoods}.
	   */
	  @DOpt(type=DOpt.Type.LinkAdder)
	  public boolean addSeafood(Collection<Seafood> seafoods) {
	    // do nothing
	    return false;
	  }
	  
	  /**
	   * @effects return seafoods
	   */
	  public Collection<Seafood> getSeafoods() {
	    return seafoods;
	  }
	  
	  /**
	   * @effects return numSeafoods
	   */
	  public int getNumSeafoods() {
	    return numSeafoods;
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
	    SeafoodByNameReport other = (SeafoodByNameReport) obj;
	    if (id != other.id)
	      return false;
	    return true;
	  }

	  @Override
	  public String toString() {
	    return "SeafoodByNameReport (" + id + ", " + name + ")";
	  }
}
