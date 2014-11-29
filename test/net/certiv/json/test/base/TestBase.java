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
// TestBaseClass ==========
package net.certiv.json.test.base;

import net.certiv.json.parser.JsonTokenFactory;
import net.certiv.json.parser.gen.JsonLexer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class TestBase extends AbstractBase {

	// public String srcDir = "";
	// public String dstDir = srcDir;
	// public String nameIn = "";
	// public String namOut = "";

	@Override
	public CommonTokenStream createLexerStream(ANTLRInputStream is) {
		JsonLexer lexer = new JsonLexer(is);
		JsonTokenFactory factory = new JsonTokenFactory(is);
		lexer.setTokenFactory(factory);
		return new CommonTokenStream(lexer);
	}
}
// TestBaseClass ==========
