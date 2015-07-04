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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenFactory;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.Pair;

import net.certiv.json.parser.gen.JsonLexer;
import net.certiv.json.types.ToStringStyle;

public class JsonTokenFactory implements TokenFactory<JsonToken> {

	public CharStream input;
	public ToStringStyle style;

	public JsonTokenFactory(CharStream input) {
		this.input = input;
		this.style = ToStringStyle.FULL;
	}

	public void toStringStyle(ToStringStyle style) {
		this.style = style;
	}

	public JsonToken create(Token prior, int type, String text) {
		JsonToken token = new JsonToken(prior);
		token.setType(type);
		token.setText(text);
		token.setChannel(Token.DEFAULT_CHANNEL);
		token.toStringStyle(style);
		return token;
	}

	@Override
	public JsonToken create(int type, String text) {
		JsonToken token = new JsonToken(type, text);
		token.toStringStyle(style);
		return token;
	}

	@Override
	public JsonToken create(Pair<TokenSource, CharStream> source, int type, String text,
			int channel, int start, int stop, int line, int charPositionInLine) {
		JsonToken token = new JsonToken(source, type, channel, start, stop);
		token.setLine(line);
		token.setCharPositionInLine(charPositionInLine);
		TokenSource tsrc = token.getTokenSource();
		token.setMode(((JsonLexer) tsrc)._mode);
		token.toStringStyle(style);
		return token;
	}
}

// TokenFactoryClass ==========
