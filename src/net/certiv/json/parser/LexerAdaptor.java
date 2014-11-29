/*******************************************************************************
 * // Copyright ==========
 * Copyright (c) 2008-2014 G Rosenberg.
 * // Copyright ==========
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 * // Contributor ==========
 *		G Rosenberg - initial API and implementation
 * // Contributor ==========
 *
 * Versions:
 * // Version ==========
 * 		1.0 - 2014.03.26: First release level code
 * 		1.1 - 2014.08.26: Updates, add Tests support
 * // Version ==========
 *******************************************************************************/
// LexerAdaptorClass ==========
package net.certiv.json.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;

public abstract class LexerAdaptor extends Lexer {

	public LexerHelper helper;

	public LexerAdaptor(CharStream input) {
		super(input);
	}

	public void setLexerHelper(LexerHelper helper) {
		this.helper = helper;
		helper.setLexer(this);
	}

	public boolean anyLA(String... terminals) {
		if (helper != null) {
			return helper.anyLA(terminals);
		}
		return false;
	}

	public boolean anyLB(String... terminals) {
		if (helper != null) {
			return helper.anyLB(terminals);
		}
		return false;
	}

	public boolean norLA(String... terminals) {
		if (helper != null) {
			return helper.norLA(terminals);
		}
		return false;
	}

	public boolean norLB(String... terminals) {
		if (helper != null) {
			return helper.norLB(terminals);
		}
		return false;
	}
}

// LexerAdaptorClass ==========
