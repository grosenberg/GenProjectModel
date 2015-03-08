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
// TokenClass ==========
package net.certiv.json.parser;

import java.util.Formatter;

import net.certiv.json.parser.gen.JsonLexer;
import net.certiv.json.util.Strings;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.Pair;

@SuppressWarnings("serial")
public class JsonToken extends CommonToken {

	public static final boolean FULL = true;
	public static final boolean BASIC = false;

	private boolean tss = FULL; // defines the toString style

	// Lexer mode
	private int _mode;

	private boolean hasStyles;
	private boolean hasBody;

	public JsonToken(int type, String text) {
		super(type, text);
	}

	public JsonToken(Pair<TokenSource, CharStream> source, int type, int channel, int start, int stop) {
		super(source, type, channel, start, stop);
	}

	public void setMode(int mode) {
		_mode = mode;
	}

	public void styles(boolean hasStyles) {
		this.hasStyles = hasStyles;
	}

	public void body(boolean hasBody) {
		this.hasBody = hasBody;
	}

	public boolean styles() {
		return hasStyles;
	}

	public boolean body() {
		return hasBody;
	}

	public boolean isTag() {
		char c = getText().charAt(0);
		return Character.isUpperCase(c);
	}

	public void toStringStyle(boolean full) {
		this.tss = full;
	}

	@Override
	public String toString() {
		String chanStr = "chan=" + channel;
		if (channel == 0) chanStr = tss ? "chan=Default" : "";
		if (channel == 1) chanStr = tss ? "chan=Hidden" : "Hidden";

		String modeStr = (tss ? "mode=" : "") + JsonLexer.modeNames[_mode];
		if (_mode == 0) modeStr = tss ? "mode=Default" : "";

		String mcStr = chanStr + " " + modeStr;
		mcStr = mcStr.trim();

		String txt = getText();
		if (txt != null) {
			txt = txt.replaceAll("\n", "\\n");
			txt = txt.replaceAll("\r", "\\r");
			txt = txt.replaceAll("\t", "\\t");
		} else {
			txt = "<no text>";
		}

		String tokenName = JsonLexer.VOCABULARY.getSymbolicName(type);

		String s = hasStyles ? "S" : "_";
		String b = hasBody ? "B" : "_";
		String t = isTag() ? "T" : "_";

		@SuppressWarnings("resource")
		Formatter fmt = new Formatter();
		if (tss) {
			fmt.format("[@%-2d %2d:%-2d <%d-%d> (%d) %s %s %s='%s']",
					getTokenIndex(), line, getCharPositionInLine(), start, stop,
					type, s + b + t, mcStr, tokenName, txt);
		} else {
			fmt.format("[@%-2d %2d:%-2d %s='%s' %s]",
					getTokenIndex(), line, getCharPositionInLine(), tokenName, txt, mcStr);
		}
		return fmt.toString() + Strings.eol;
	}
}

// TokenClass ==========
