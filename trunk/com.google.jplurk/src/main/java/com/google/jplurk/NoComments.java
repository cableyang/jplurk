/*
 * no_comments: If set to 1, then responses are disabled for this plurk.
 * If set to 2, then only friends can respond to this plurk.
 * and set to 0, it will be a normal plurk.
 */

package com.google.jplurk;

/**
 *
 * @author Askeing
 */
public enum NoComments {
    False("0"), True("1"), Friends("2");

	private String noComments;

	private NoComments(String noComments) {
		this.noComments = noComments;
	}

	@Override
	public String toString() {
		return noComments;
	}


}

