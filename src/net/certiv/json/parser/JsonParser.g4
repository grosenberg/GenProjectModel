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
// ParserGrammar ==========
parser grammar JsonParser;

options {
	tokenVocab = JsonLexer;
}

@header {
	package net.certiv.json.parser.gen;
}

@members {
	public boolean LA(int... expected) {
		int t = ((CommonTokenStream) _input).LA(1);
		for (int e : expected) {
			if (e == t) return true;
		}
		return false;
	}
}

// PARSER /////////////////////////////////////////////

json
	: ( object | array )* EOF
	;	

object
	: OBrace element (Sep element)* CBrace
	| OBrace CBrace
	;

element
	: String Colon value
	;	

array
	: OBracket f=value (Sep g+=value)* CBracket
	| OBracket CBracket
	;


value
	: String
	| Number
	| object  
	| array  
	| True 
	| False
	| Null
	;

// ParserGrammar ==========
