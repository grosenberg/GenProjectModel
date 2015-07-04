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
// TestWordClass ==========
package net.certiv.json.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.certiv.json.IOProcessor;
import net.certiv.json.converter.Converter;
import net.certiv.json.test.base.TestBase;
import net.certiv.json.util.Log;

public class TestWord extends TestBase {

	private Converter converter;

	@BeforeMethod
	public void setUp() throws Exception {
		Log.setTestMode(true);
		String[] args = { "-s" };
		IOProcessor processor = new IOProcessor(args);
		converter = new Converter(processor);
	}

	@AfterMethod
	public void tearDown() throws Exception {
		converter = null;
	}

	@Test
	public void testLexBold() throws Exception {
		String source = "This is a *simple* sentence." + EOL;
		String found = lexSource(source, true, true);
		String expecting = "[@0   1:0  Word='This' ]" + EOL
				+ "[@1   1:4  Ws=' ' Hidden]" + EOL
				+ "[@2   1:5  Word='is' ]" + EOL
				+ "[@3   1:7  Ws=' ' Hidden]" + EOL
				+ "[@4   1:8  Word='a' ]" + EOL
				+ "[@5   1:9  Ws=' ' Hidden]" + EOL
				+ "[@6   1:10 Star='*' ]" + EOL
				+ "[@7   1:11 Word='simple' ]" + EOL
				+ "[@8   1:17 Star='*' ]" + EOL
				+ "[@9   1:18 Ws=' ' Hidden]" + EOL
				+ "[@10  1:19 Word='sentence.' ]" + EOL
				+ "[@11  1:28 Blankline='rn' ]" + EOL//
				+ "[@12  2:30 Eof='<EOF>' ]" + EOL;
		assertEquals(found, expecting);
	}

	@Test
	public void testConvertBold() {
		String source = "This is a *simple* sentence." + EOL;
		String result = converter.convert("", source);
		String expecting = "<p>This is a <b>simple</b> sentence.</p>" + EOL;
		assertEquals(expecting, result);
	}

	@Test
	public void testLexUnderscore() throws Exception {
		String source = "Test: _underline_ text." + EOL;
		String found = lexSource(source, true, true);
		String expecting = "[@0   1:0  Word='Test:' ]" + EOL
				+ "[@1   1:5  Ws=' ' Hidden]" + EOL
				+ "[@2   1:6  Underscore='_' ]" + EOL
				+ "[@3   1:7  Word='underline' ]" + EOL
				+ "[@4   1:16 Underscore='_' ]" + EOL
				+ "[@5   1:17 Ws=' ' Hidden]" + EOL
				+ "[@6   1:18 Word='text.' ]" + EOL
				+ "[@7   1:23 Blankline='rn' ]" + EOL
				+ "[@8   2:25 Eof='<EOF>' ]" + EOL;
		assertEquals(found, expecting);
	}

	@Test
	public void testWordPunct() throws Exception {
		String source = "And, just_a simple-test. Here & now: a *'special'* don't." + EOL;
		String found = lexSource(source, true, true);
		String expecting = "[@0   1:0  Word='And,' ]" + EOL
				+ "[@1   1:4  Ws=' ' Hidden]" + EOL
				+ "[@2   1:5  Word='just_a' ]" + EOL
				+ "[@3   1:11 Ws=' ' Hidden]" + EOL
				+ "[@4   1:12 Word='simple-test.' ]" + EOL
				+ "[@5   1:24 Ws=' ' Hidden]" + EOL
				+ "[@6   1:25 Word='Here' ]" + EOL
				+ "[@7   1:29 Ws=' ' Hidden]" + EOL
				+ "[@8   1:30 Word='&' ]" + EOL
				+ "[@9   1:31 Ws=' ' Hidden]" + EOL
				+ "[@10  1:32 Word='now:' ]" + EOL
				+ "[@11  1:36 Ws=' ' Hidden]" + EOL
				+ "[@12  1:37 Word='a' ]" + EOL
				+ "[@13  1:38 Ws=' ' Hidden]" + EOL
				+ "[@14  1:39 Star='*' ]" + EOL
				+ "[@15  1:40 SQuote=''' ]" + EOL
				+ "[@16  1:41 Word='special' ]" + EOL
				+ "[@17  1:48 SQuote=''' ]" + EOL
				+ "[@18  1:49 Star='*' ]" + EOL
				+ "[@19  1:50 Ws=' ' Hidden]" + EOL
				+ "[@20  1:51 Word='don't.' ]" + EOL
				+ "[@21  1:57 Blankline='rn' ]" + EOL
				+ "[@22  2:59 Eof='<EOF>' ]" + EOL;
		assertEquals(found, expecting);
	}
}

// TestWordClass ==========
