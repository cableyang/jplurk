package com.googlecode.jplurk.behavior;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.jplurk.net.Request;



public interface IBehavior {
	
	public boolean action(final Request params, Object arg);

}
