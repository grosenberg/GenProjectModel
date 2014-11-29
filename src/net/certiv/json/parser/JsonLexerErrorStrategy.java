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
// LexerErrorStrategyClass ==========
package net.certiv.json.parser;

import net.certiv.json.parser.gen.JsonLexer;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;

/**
 * Base class for the Lexer. Combines functionality for <br>
 * <ul>
 * <li>lexer error strategy</li>
 * <li>various helper routines</li>
 * <ul>
 * 
 * @author Gbr
 * 
 */
public class JsonLexerErrorStrategy extends JsonLexer {

	// ///// Error strategy /////////////////////////////
	public JsonLexerErrorStrategy(CharStream input) {
		super(input);
	}

	public void recover(LexerNoViableAltException e) {
		// throw new RuntimeException(e); // Bail out
		super.recover(e);
	}

	// ///// Parse stream management ////////////////////

}

// LexerErrorStrategyClass ==========
