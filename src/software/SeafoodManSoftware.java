package software;

import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.software.DomainAppToolSoftware;
import model.Country;
import model.DomesticSeafood;
import model.ForeignSeafood;
import model.Seafood;

public class SeafoodManSoftware extends DomainAppToolSoftware{

	private static final Class[] model = {
		Seafood.class,
		DomesticSeafood.class,
		ForeignSeafood.class,
		Country.class
	};
	public static void main(String[] args) throws NotPossibleException {
		new SeafoodManSoftware().exec(args);
	}

	@Override
	protected Class[] getModel() {
		return model;
	}
}
