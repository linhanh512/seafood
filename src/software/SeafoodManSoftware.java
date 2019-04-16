package software;

import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.software.DomainAppToolSoftware;
import model.Country;
import model.Customer;
import model.DomesticSeafood;
import model.ForeignSeafood;
import model.Purchaser;
import model.Seafood;
import model.TypeOfSeafood;
import model.reports.*;
import model.reports.CustomerByNameReport;
import model.reports.SeafoodByNameReport;


/**
 * @overview 
 *  Encapsulate the basic functions for setting up and running a software given its domain model.  
 *  
 * @author 
 *
 * @version 
 */
public class SeafoodManSoftware extends DomainAppToolSoftware {
  
  // the domain model of software
  private static final Class[] model = {
      Seafood.class,
      Country.class, 
      Customer.class,
      Purchaser.class,
      DomesticSeafood.class,
      ForeignSeafood.class,
      TypeOfSeafood.class,
      // reports
      SeafoodByNameReport.class,
      CustomerByNameReport.class
  };
  
  /* (non-Javadoc)
   * @see vn.com.courseman.software.Software#getModel()
   */
  /**
   * @effects 
   *  return {@link #model}.
   */
  @Override
  protected Class[] getModel() {
    return model;
  }

  /**
   * The main method
   * @effects 
   *  run software with a command specified in args[0] and with the model 
   *  specified by {@link #getModel()}. 
   *  
   *  <br>Throws NotPossibleException if failed for some reasons.
   */
  public static void main(String[] args) throws NotPossibleException {
    new SeafoodManSoftware().exec(args);
  }
}
