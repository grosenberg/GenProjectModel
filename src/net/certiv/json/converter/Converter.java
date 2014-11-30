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
// ConverterClass ==========
package net.certiv.json.converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import net.certiv.json.IOProcessor;
import net.certiv.json.parser.JsonErrorListener;
import net.certiv.json.parser.JsonTokenFactory;
import net.certiv.json.parser.gen.JsonLexer;
import net.certiv.json.parser.gen.JsonParser;
import net.certiv.json.util.Log;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Converter {

	private IOProcessor processor;

	public Converter(IOProcessor processor) {
		super();
		this.processor = processor;
	}

	public String convert(String srcData) {
		return convert(srcData, new PhaseState());
	}

	public String convert(String srcData, PhaseState state) {
		String lastError = "<none>";
		try {
			lastError = "Failure in acquiring input stream.";
			ByteArrayInputStream is = new ByteArrayInputStream(srcData.getBytes());
			ANTLRInputStream input = new ANTLRInputStream(is);

			lastError = "Failure in generating lexer token stream.";
			JsonLexer lexer = new JsonLexer(input);
			// lexer.setLexerHelper(new LexerHelper());
			JsonTokenFactory factory = new JsonTokenFactory(input);
			lexer.setTokenFactory(factory);
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			lastError = "Failure in parser production.";
			JsonParser parser = new JsonParser(tokens);
			parser.setTokenFactory(factory);
			parser.removeErrorListeners(); // remove ConsoleErrorListener
			parser.addErrorListener(new JsonErrorListener());
			// parser.setErrorHandler(new JsonParserErrorStrategy());
			ParseTree tree = parser.json();

			ParseTreeWalker walker = new ParseTreeWalker();

			lastError = "Failure in parse phase 1.";
			state.tokens = tokens;
			JsonPhase01 phase01 = new JsonPhase01(state, processor);
			phase01.collectComments(true);
			walker.walk(phase01, tree);

			lastError = "Failure in parse phase 2.";
			JsonPhase02 phase02 = new JsonPhase02(phase01, processor);
			walker.walk(phase02, tree);

			if (!JsonPhase02.statusResolved) {
				Log.warn(this, "Failure to resolve source description");
				return "";
			}

			lastError = "Failure in parse phase 3.";
			JsonPhase03 phase03 = new JsonPhase03(phase02, processor);
			walker.walk(phase03, tree);
			Log.info(this, "Convertion complete.");

			Log.info(this, "Convertion complete.");
			return state.doc.toString();

		} catch (IOException e) {
			Log.error(this, lastError, e);
			return "";
		}
	}
}

// ConverterClass ==========
