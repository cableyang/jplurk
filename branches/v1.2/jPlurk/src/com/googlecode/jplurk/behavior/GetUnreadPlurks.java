package com.googlecode.jplurk.behavior;


import com.googlecode.jplurk.Constants;
import com.googlecode.jplurk.net.Request;

public class GetUnreadPlurks implements IBehavior{

	@Override
	public boolean action(Request params, Object arg) {
		params.setEndPoint(Constants.GET_UNREAD_PLURK_URL);
		params.addParam("fetch_responses", "false");
//	    def get_unread_plurks(fetch_responses=false)
//	      return false unless @logged_in
//	      params = {
//	        :fetch_responses => fetch_responses
//	      }
//	      data = statuses(plurk_to_json(request("/TimeLine/getUnreadPlurks", :method => :get, :params => params )))
//	      return data
//	    end
		return true;
	}

}
