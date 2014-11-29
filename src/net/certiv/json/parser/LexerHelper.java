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
// LexerHelperClass ==========
package net.certiv.json.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Lexer;

public class LexerHelper {

	private Lexer lexer;

	public LexerHelper() {
		super();
	}

	public LexerHelper(Lexer lexer) {
		super();
		this.lexer = lexer;
	}

	public void setLexer(Lexer lexer) {
		this.lexer = lexer;
	}

	public boolean anyLA(String... terminals) {
		return !norLA(terminals);
	}

	public boolean norLA(String... terminals) {
		ANTLRInputStream input = (ANTLRInputStream) lexer.getInputStream();

		for (String str : terminals) {
			int index = 0;
			for (int idx = 0; idx < str.length(); idx++) {
				if (input.LA(index + 1) == IntStream.EOF) {
					break;
				}
				char s = str.charAt(idx);
				char la = (char) input.LA(index + 1);
				if (s != la) {
					break;
				}
				index++;
			}
			if (index == str.length()) {
				return false;
			}
		}
		return true;
	}

	public boolean anyLB(String... terminals) {
		return !norLB(terminals);
	}

	public boolean norLB(String... terminals) {
		ANTLRInputStream input = (ANTLRInputStream) lexer.getInputStream();

		for (String str : terminals) {
			int index = 0;
			for (int idx = str.length() - 1; idx >= 0; idx--) {
				if (input.index() < str.length() - 1) {
					break;
				}
				char s = str.charAt(idx);
				char lb = (char) input.LA(index - 1);
				if (s != lb) {
					break;
				}
				index--;
			}
			if (index * -1 == str.length()) {
				return false;
			}
		}
		return true;
	}

	public int skipToEol(ANTLRInputStream input, int index) {
		while (input.LA(index) != IntStream.EOF && input.LA(index) != '\n') {
			index++;
		}
		return index;
	}

	public int skipToEoc(ANTLRInputStream input, int index) {
		while (input.LA(index) != IntStream.EOF) {
			if (input.LA(index) == '/' && input.LA(index - 1) == '*') {
				return index;
			}
			index++;
		}
		return index;
	}
}
// LexerHelperClass ==========
