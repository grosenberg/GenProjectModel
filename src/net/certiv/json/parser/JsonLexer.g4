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
// LexerGrammar ==========
lexer grammar JsonLexer;

@header {
	package net.certiv.json.parser.gen;
}

@members {
	// semantic alias for the default mode
	public static final int Base = DEFAULT_MODE;
}

// LEXER /////////////////////////////////////////////

////////////// Default Mode (alias Base) ///////////////////
// mode Base;

Colon	: COLON		;
Sep		: COMMA		;
OBrace	: LBRACE 	;
CBrace 	: RBRACE 	;
OBracket: LBRACKET 	;
CBracket: RBRACKET 	;
True	: TRUE 		;
False	: FALSE 	;
Null	: NULL 		;


// -------------------
// Strings and Numbers

Number	: INT FRAC EXP? 
		| INT EXP 
		| INT
		;

String	: LITERAL ;		


// Multi-line comments handled as a single token
Comment
    : ML_COMMENT  -> channel(HIDDEN) 
    ;

// group one or more consecutive line comments together,  
// excluding the final line terminal(s), as a single token
CommentLine
	: SL_COMMENT -> channel(HIDDEN)
	;

// all other horizontal and vertical whitespace is separately tokenized
HorzWS
	: HWS	-> channel(HIDDEN)
	;

VertWS
	: VWS	-> channel(HIDDEN)
	;


/////////////////// FRAGMENTS //////////////////

// -------------------
// Constants

fragment TRUE		: 'true'  ;
fragment FALSE		: 'false' ;
fragment NULL		: 'null'  ;

// -------------------
// Letters, Numbers and Quoted things

fragment IDENT			: LETTER | DIGIT | UNDERSCORE | MINUS ;
fragment LETTER			: LETTER_UPPER | LETTER_LOWER ;
fragment LETTER_UPPER	: 'A'..'Z' ;
fragment LETTER_LOWER	: 'a'..'z' ;
fragment DIGIT			: '0'..'9' ;
fragment INT			: [-]? ('0' | [1-9] DIGIT*);
fragment FRAC			: '.' DIGIT+;
fragment EXP			: [eE] [+\-]? DIGIT+;

fragment HEX 		:	[0-9a-fA-F] ;
fragment UNICODE	:   'u' HEX HEX HEX HEX ;

fragment ESC		// Any escaped character
	:	'\\' 
		(	[btnfr"'\\/]		// ordinary escaped characters
		|	UNICODE				// Java style Unicode escape sequence
		)
	;

fragment LITERAL
    :  '"'  ( ESC_SEQ | ~["\\] )* '"'
	|  '\'' ( ESC_SEQ | ~['\\] )* '\''
    ;


// -----------
// Punctuation

fragment AT			: '@'	;
fragment COLON		: ':'	;
fragment SEMI		: ';'	;
fragment LBRACE		: '{'	;
fragment RBRACE		: '}'	;
fragment LBRACKET	: '['	;
fragment RBRACKET	: ']'	;
fragment LPAREN		: '('	;
fragment RPAREN		: ')'	;

fragment LMARK		: '<'	;
fragment RMARK		: '|>'	;
fragment LTMPL		: '{{'	; 
fragment RTMPL		: '}}'	; 

fragment DOT		: '.'	;
fragment HASH		: '#'	;
fragment REF		: '$'	;

fragment COMMA		: ','	;
fragment UNDERSCORE	: '_'	;
fragment PIPE		: '|'	;

fragment OP			: PLUS | MINUS | MULT | DIV | MOD ;
fragment PLUS		: '+'	;
fragment MINUS		: '-'	;
fragment MULT		: '*'	;
fragment DIV		: '/'	;
fragment MOD		: '%'	;

fragment FUNCTION	: ADD | SUB | MUL_EQ | DIV_EQ | MOD_EQ | EQV | NEQ | LT | LTE | GT | GTE | EQ ;
fragment EQ			: '='	;
fragment EQV		: '=='	;
fragment NEQ		: '!='	;
fragment LT			: '<'	;
fragment LTE		: '<='	;
fragment GT			: '>'	;
fragment GTE		: '>='	;
fragment ADD		: '+='	;
fragment SUB		: '-='	;
fragment MUL_EQ		: '*='	;
fragment DIV_EQ		: '/='	;
fragment MOD_EQ		: '%='	;


// ----------
// Whitespace, etc.

fragment SL_COMMENT		: '//' ~'\n'* ( '\n' HWS* '//' ~'\n'* )*	;

fragment ML_COMMENT		: '/*' .*? '*/'
						| '<!--' .*? '-->' 
						;

fragment HWS			:  [ \t\r] ;
fragment VWS			:  [\n\u000C] ;

fragment ESC_SEQ		:   '\\' ('b'|'t'|'n'|'f'|'r') ;

// LexerGrammar ==========
