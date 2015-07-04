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
import net.certiv.json.parser.gen.JsonParser.JsonContext;
import net.certiv.json.util.Log;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Converter {

	private IOProcessor processor;
	private String lastError = "<none>";

	public Converter(IOProcessor processor) {
		super();
		this.processor = processor;
	}

	public String convert(String srcName, String srcData) {
		return convert(srcName, srcData, new PhaseState());
	}

	public String convert(String srcName, String srcData, PhaseState state) {
		try {
			JsonContext tree = parse(srcName, srcData, state);
			ParseTreeWalker walker = new ParseTreeWalker();
			JsonPhase01 phase01 = processPhase01(tree, walker, state);
			JsonPhase02 phase02 = processPhase02(tree, walker, phase01);
			JsonPhase03 phase03 = processPhase03(tree, walker, phase02);
			Log.info(this, "Convertion complete.");
			return phase03.state.toString();
		} catch (IOException e) {
			Log.error(this, lastError, e);
			return "";
		}
	}

	private JsonContext parse(String srcName, String srcData, PhaseState state) throws IOException {
		lastError = "Failure in acquiring input stream.";
		ByteArrayInputStream is = new ByteArrayInputStream(srcData.getBytes());
		ANTLRInputStream input = new ANTLRInputStream(is);
		input.name = srcName;

		lastError = "Failure in generating lexer token stream.";
		JsonLexer lexer = new JsonLexer(input);
		JsonTokenFactory factory = new JsonTokenFactory(input);
		lexer.setTokenFactory(factory);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		state.tokens = tokens;

		lastError = "Failure in parser production.";
		JsonParser parser = new JsonParser(tokens);
		parser.setTokenFactory(factory);
		parser.removeErrorListeners(); // remove ConsoleErrorListener
		parser.addErrorListener(new JsonErrorListener());
		// parser.setErrorHandler(new JsonParserErrorStrategy());
		return parser.json();

	}

	public JsonPhase01 processPhase01(JsonContext tree, ParseTreeWalker walker, PhaseState state) {
		lastError = "Failure in parse phase 1.";
		JsonPhase01 phase01 = new JsonPhase01(state, processor);
		phase01.collectComments(true);
		walker.walk(phase01, tree);
		return phase01;
	}

	public JsonPhase02 processPhase02(JsonContext tree, ParseTreeWalker walker, JsonPhase01 phase01) {
		lastError = "Failure in parse phase 2.";
		JsonPhase02 phase02 = new JsonPhase02(phase01, processor);
		walker.walk(phase02, tree);
		if (!JsonPhase02.statusResolved) {
			Log.warn(this, "Failure to resolve source description in phase2");
		}
		return phase02;
	}

	public JsonPhase03 processPhase03(JsonContext tree, ParseTreeWalker walker, JsonPhase02 phase02) {
		lastError = "Failure in parse phase 3.";
		JsonPhase03 phase03 = new JsonPhase03(phase02, processor);
		walker.walk(phase03, tree);
		return phase03;
	}
}

// ConverterClass ==========
