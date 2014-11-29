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
// TokenFactoryClass ==========
package net.certiv.json.parser;

import net.certiv.json.parser.gen.JsonLexer;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenFactory;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.Pair;

public class JsonTokenFactory implements TokenFactory<JsonToken> {

	public CharStream input;

	public JsonTokenFactory(CharStream input) {
		this.input = input;
	}

	@Override
	public JsonToken create(int type, String text) {
		return new JsonToken(type, text);
	}

	@Override
	public JsonToken create(Pair<TokenSource, CharStream> source, int type, String text,
			int channel, int start, int stop, int line, int charPositionInLine) {
		JsonToken token = new JsonToken(source, type, channel, start, stop);
		token.setLine(line);
		token.setCharPositionInLine(charPositionInLine);
		TokenSource tsrc = token.getTokenSource();
		token.setMode(((JsonLexer) tsrc)._mode);
		return token;
	}
}

// TokenFactoryClass ==========
