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
// ErrorListenerClass ==========
package net.certiv.json.parser;

import java.util.Collections;
import java.util.List;

import net.certiv.json.util.Log;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

public class JsonErrorListener extends BaseErrorListener {

	public int lastError = -1;

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {

		Parser parser = (Parser) recognizer;
		TokenStream tokens = parser.getInputStream();

		Token offSymbol = (Token) offendingSymbol;
		int thisError = offSymbol.getTokenIndex();
		if (offSymbol.getType() == -1 && thisError == tokens.size() - 1) {
			Log.debug(this, "Incorrect error: " + msg);
			return;
		}
		if (thisError > lastError + 20) {
			lastError = thisError - 20;
		}
		for (int idx = lastError + 1; idx <= thisError; idx++) {
			Token token = tokens.get(idx);
			if (token.getChannel() != Token.HIDDEN_CHANNEL)
				Log.error(this, token.toString());
		}
		lastError = thisError;

		List<String> stack = parser.getRuleInvocationStack();
		Collections.reverse(stack);

		Log.error(this, "rule stack: " + stack);
		Log.error(this, "line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + msg);
	}
}
// ErrorListenerClass ==========
