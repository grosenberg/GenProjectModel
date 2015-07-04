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

import java.io.File;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import net.certiv.json.IOProcessor;
import net.certiv.json.converter.Converter;
import net.certiv.json.converter.JsonPhase01;
import net.certiv.json.converter.JsonPhase02;
import net.certiv.json.converter.JsonPhase03;
import net.certiv.json.converter.PhaseState;
import net.certiv.json.parser.JsonErrorListener;
import net.certiv.json.parser.JsonToken;
import net.certiv.json.parser.JsonTokenFactory;
import net.certiv.json.parser.gen.JsonLexer;
import net.certiv.json.parser.gen.JsonParser;
import net.certiv.json.parser.gen.JsonParser.JsonContext;
import net.certiv.json.util.FileUtils;
import net.certiv.json.util.Log;

public class TestBase extends AbstractBase {

	public static final String DataDir = "test.data";
	public static final String ResultDir = "test.results";
	public static final String LexExt = "Lex.txt";
	public static final String ParseExt = "Parse.txt";

	public TestBase() {
		super();
		Log.setTestMode(true);
	}

	@Override
	public String getBaseDir() {
		return null;
	}

	@Override
	public String getTestExt() {
		return null;
	}

	@Override
	public String getTokenString(Token token) {
		return ((JsonToken) token).toString();
	}

	@Override
	public CommonTokenStream createLexerStream(ANTLRInputStream is) {
		JsonLexer lexer = new JsonLexer(is);
		JsonTokenFactory factory = new JsonTokenFactory(is);
		lexer.setTokenFactory(factory);
		return new CommonTokenStream(lexer);
	}

	public String produceTree(CommonTokenStream tokens, boolean sysout) {
		PhaseState state = new PhaseState();
		state.tokens = tokens;

		JsonParser parser = new JsonParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new JsonErrorListener());
		JsonContext tree = parser.json();
		ParseTreeWalker walker = new ParseTreeWalker();

		Converter conv = new Converter(new IOProcessor(new String[] { "-s" }));
		JsonPhase01 phase01 = conv.processPhase01(tree, walker, state);
		JsonPhase02 phase02 = conv.processPhase02(tree, walker, phase01);
		JsonPhase03 phase03 = conv.processPhase03(tree, walker, phase02);

		String result = phase03.getState().doc.toString();
		if (sysout) System.out.println(result);
		return result;
	}

	public String readSrcString(String name) {
		return readString(DataDir, name, getTestExt());
	}

	public String readUpdateLexString(String name, String found) {
		String expecting = readString(ResultDir, name, LexExt);
		if (expecting.length() == 0) {
			writeString(ResultDir, name, found, LexExt);
		}
		return expecting;
	}

	public String readUpdateParseTree(String name, String found) {
		String expecting = readString(ResultDir, name, ParseExt);
		if (expecting.length() == 0) {
			writeString(ResultDir, name, found, ParseExt);
		}
		return expecting;
	}

	private String readString(String dir, String name, String ext) {
		name = convertName(dir, name, ext);
		File f = new File(name);
		String data = "";
		if (f.isFile()) {
			try {
				data = FileUtils.read(f);
			} catch (IOException e) {
				System.err.println("Read failed: " + e.getMessage());
			}
		}
		return data;
	}

	private void writeString(String dir, String name, String data, String ext) {
		name = convertName(dir, name, ext);
		File f = new File(name);
		File p = f.getParentFile();
		if (!p.exists()) {
			if (!p.mkdirs()) {
				System.err.println("Failed to create directory: " + p.getAbsolutePath());
				return;
			}
		} else if (p.isFile()) {
			System.err.println("Cannot make directory: " + p.getAbsolutePath());
			return;
		}

		if (f.exists() && f.isFile()) {
			f.delete();
		}
		try {
			FileUtils.write(f, data, false);
		} catch (IOException e) {
			System.err.println("Write failed: " + e.getMessage());
		}
	}

	private String convertName(String dir, String name, String ext) {
		int idx = name.lastIndexOf('.');
		if (idx > 0) {
			name = name.substring(0, idx);
		}
		return FileUtils.concat(getBaseDir(), dir, name + ext);
	}
}
