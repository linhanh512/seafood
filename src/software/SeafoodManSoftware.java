package software;

import controller.ExportSeafood;
import controller.ImportSeafood;
import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.software.DomainAppToolSoftware;

import model.Country;
import model.Customer;
import model.DomesticSeafood;
import model.ForeignSeafood;
import model.OrderRow;
import model.OrderTable;
import model.Purchaser;
import model.Seafood;
import model.Seller;
import model.TypeOfSeafood;


import model.reports.CustomerByNameReport;
import model.reports.ImportSeafoodByDateReport;
import model.reports.SeafoodByNameReport;


/**
 * @overview 
 *  Encapsulate the basic functions for setting up and running a software given its domain model.  
 *  
 * @author dmle
 *
 * @version 
 */
public class SeafoodManSoftware extends DomainAppToolSoftware {
  
  // the domain model of software
  private static final Class[] model = {

      //seafood
	  Seafood.class,
      DomesticSeafood.class,
      ForeignSeafood.class,
      Country.class, 
      //customer
      Customer.class,
      Purchaser.class,
      Seller.class,
      //action
      
      ImportSeafood.class,
      ExportSeafood.class,
      TypeOfSeafood.class,
      OrderTable.class,
      OrderRow.class, 
      // reports
      ImportSeafoodByDateReport.class,
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
