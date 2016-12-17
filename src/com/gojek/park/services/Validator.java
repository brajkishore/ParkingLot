/**
 * 
 */
package com.gojek.park.services;

/**
 * @author braj.kishore
 *
 */
public class Validator implements IValidator {

	/* (non-Javadoc)
	 * @see com.gojek.park.services.IValidator#validate(java.lang.String)
	 */
	@Override
	public boolean validate(String value) {
		// TODO Auto-generated method stub
		
		if(value==null ||value.isEmpty())
			return false;
		
		return true;
	}

}
