package com.googlecode.jplurk;

import com.googlecode.jplurk.net.Request;



public interface Behavior {

	public boolean action(final Request params, Object arg);

}
