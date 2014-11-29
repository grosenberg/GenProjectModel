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
// AbstractBaseClass ==========
package net.certiv.json.test.base;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import net.certiv.json.parser.JsonToken;
import net.certiv.json.parser.gen.JsonParser;
import net.certiv.json.parser.gen.JsonParserBaseListener;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;
import org.junit.Assert;

public abstract class AbstractBase {

	public static final String TAB = "\t";
	public static final String EOL = System.getProperty("line.separator");
	public static final String PSEP = System.getProperty("path.separator");

	public String tmpdir = null;

	/** reset during setUp and set to true if we find a problem */
	protected boolean lastTestFailed = false;

	public void setUpTempDir() throws Exception { // new output dir for each test
		lastTestFailed = false; // hope for the best, but set to true in asserts that fail
		long time = System.currentTimeMillis();
		tmpdir = new File("/tmp", getClass().getSimpleName() + "-" + time).getAbsolutePath();
		mkdir(tmpdir);
	}

	public void tearDownTempDir() throws Exception { // remove tmpdir if no error.
		if (!lastTestFailed) eraseTempDir();
	}

	public String lexSource(String source, boolean output, boolean style) {
		CommonTokenStream tokens = produceTokens(source);
		tokens.fill();
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens.getTokens()) {
			((JsonToken) token).toStringStyle(style);
			sb.append(token.toString());
			if (output) System.out.print(token.toString());
		}
		return sb.toString();
	}

	public CommonTokenStream produceTokens(String source) {
		ANTLRInputStream is = new ANTLRInputStream(source);
		return createLexerStream(is);
	}

	public abstract CommonTokenStream createLexerStream(ANTLRInputStream is);

	public List<String> getTokenTypes(LexerGrammar lg, ATN atn, CharStream input) {
		LexerATNSimulator interp = new LexerATNSimulator(atn, new DFA[] { new DFA(
				atn.modeToStartState.get(Lexer.DEFAULT_MODE)) }, null);
		List<String> tokenTypes = new ArrayList<String>();
		int ttype;
		boolean hitEOF = false;
		do {
			if (hitEOF) {
				tokenTypes.add("EOF");
				break;
			}
			int t = input.LA(1);
			ttype = interp.match(input, Lexer.DEFAULT_MODE);
			if (ttype == Token.EOF) {
				tokenTypes.add("EOF");
			}
			else {
				tokenTypes.add(lg.typeToTokenList.get(ttype));
			}

			if (t == IntStream.EOF) {
				hitEOF = true;
			}
		} while (ttype != Token.EOF);
		return tokenTypes;
	}

	public ParseTree produceTree(CommonTokenStream tokens) {
		JsonParser parser = new JsonParser(tokens);
		return parser.json();
	}

	protected void checkSymbols(Grammar g, String rulesStr, String allValidTokensStr) throws Exception {
		String[] typeToTokenName = g.getTokenNames();
		Set<String> tokens = new HashSet<String>();
		for (int i = 0; i < typeToTokenName.length; i++) {
			String t = typeToTokenName[i];
			if (t != null) {
				if (t.startsWith(Grammar.AUTO_GENERATED_TOKEN_NAME_PREFIX)) {
					tokens.add(g.getTokenDisplayName(i));
				}
				else {
					tokens.add(t);
				}
			}
		}

		// make sure expected tokens are there
		StringTokenizer st = new StringTokenizer(allValidTokensStr, ", ");
		while (st.hasMoreTokens()) {
			String tokenName = st.nextToken();
			assertTrue("token " + tokenName + " expected, but was undefined",
					g.getTokenType(tokenName) != Token.INVALID_TYPE);
			tokens.remove(tokenName);
		}
		// make sure there are not any others (other than <EOF> etc...)
		for (String tokenName : tokens) {
			assertTrue("unexpected token name " + tokenName,
					g.getTokenType(tokenName) < Token.MIN_USER_TOKEN_TYPE);
		}

		// make sure all expected rules are there
		st = new StringTokenizer(rulesStr, ", ");
		int n = 0;
		while (st.hasMoreTokens()) {
			String ruleName = st.nextToken();
			assertNotNull("rule " + ruleName + " expected", g.getRule(ruleName));
			n++;
		}
		Assert.assertEquals("number of rules mismatch; expecting " + n + "; found " + g.rules.size(), n, g.rules.size());
	}

	public void assertEquals(Object expected, Object actual) {
		try {
			Assert.assertEquals(expected, actual);
		} catch (Error e) {
			lastTestFailed = true;
			throw e;
		}
	}

	protected void mkdir(String dir) {
		File f = new File(dir);
		f.mkdirs();
	}

	protected void eraseTempDir() {
		File tmpdirF = new File(tmpdir);
		if (tmpdirF.exists()) {
			eraseFiles();
			tmpdirF.delete();
		}
	}

	protected void eraseFiles() {
		if (tmpdir == null) return;
		File tmpdirF = new File(tmpdir);
		String[] files = tmpdirF.list();
		for (int i = 0; files != null && i < files.length; i++) {
			new File(tmpdir + PSEP + files[i]).delete();
		}
	}

	public class ProcessEveryRule extends JsonParserBaseListener {

		@Override
		public void enterEveryRule(ParserRuleContext ctx) {
			super.enterEveryRule(ctx);
		}

		@Override
		public void exitEveryRule(ParserRuleContext ctx) {
			super.exitEveryRule(ctx);
		}
	}
}

// AbstractBaseClass ==========
