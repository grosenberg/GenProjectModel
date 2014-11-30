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
// StringsClass ==========
package net.certiv.json.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.certiv.json.converter.Value;

import org.apache.commons.io.FilenameUtils;

public class Strings {

	/** Platform dependent end-of-line marker */
	public static final String eol = System.lineSeparator();
	/** Platform dependent path separator mark as char */
	public static final char pathSep = File.separatorChar;
	/** Platform dependent path separator mark as string */
	public static final String pathSepStr = File.separator;
	/** Platform dependent path separator mark */
	public static final String pkgSep = ".";
	/** classpath (and unix) separator) */
	public static final String STD_SEPARATOR = "/";
	// Windows separator character.
	private static final String WINDOWS_SEPARATOR = "\\\\";

	public static final String SP = " ";
	public static final String RN = "\r\n";
	public static final String DOT = ".";
	public static final String STAR = "*";
	public static final String HASH = "#";

	/** Reference characters */
	private static char[] R = { '$', '@', '.', '#' };

	/** Quoting character pairs */
	private static char[][] Q = {
			{ '\'', '\'' },
			{ '\"', '\"' },
			{ '[', ']' },
			{ '(', ')' },
			{ '{', '}' },
			{ '<', '>' }
	};

	public static String asString(ArrayList<String> values, boolean asPrefix, String sep) {
		StringBuilder sb = new StringBuilder();
		String pf = asPrefix ? sep : "";
		String sf = asPrefix ? "" : sep;
		for (String value : values) {
			sb.append(pf + value + sf);
		}
		if (!asPrefix) { // removes trailing sep by default
			int beg = sb.length() - 1 - sep.length();
			sb.delete(beg, sb.length());
		}
		return sb.toString();
	}

	public static boolean numeric(String value) {
		if (value.length() == 0) return false; // empty string is a string
		try {
			Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Convert a list of strings to a standard csv representation
	 */
	public static String toCsv(List<String> strs) {
		StringBuilder sb = new StringBuilder();
		for (String s : strs) {
			sb.append(s + ", ");
		}
		if (sb.length() > 1) sb.setLength(sb.length() - 2);
		return sb.toString();
	}

	public static String csvValueList(List<Value> valueList) {
		return csvValueList(valueList, ", ");
	}

	public static String csvValueList(List<Value> valueList, String sep) {
		if (valueList == null) return "";
		StringBuilder sb = new StringBuilder();
		for (Value v : valueList) {
			sb.append(v.basis + sep);
		}
		return sb.toString().substring(0, sb.length() - sep.length());
	}

	public static String csvList(List<String> stringList) {
		if (stringList == null) return "";
		StringBuilder sb = new StringBuilder();
		for (String str : stringList) {
			sb.append(str + ", ");
		}
		int len = sb.length();
		sb.delete(len - 2, len);
		return sb.toString();
	}

	public static List<String> csvToList(String csv) {
		String[] parts = csv.split(",");
		return Arrays.asList(parts);
	}

	/**
	 * Remove leading reference identifier. No error if the identifier is not present.
	 */
	public static String varName(String varRef) {
		for (int idx = 0; idx < R.length; idx++) {
			if (varRef.charAt(0) == R[idx]) {
				return varRef.substring(1);
			}
		}
		return varRef;
	}

	/**
	 * Remove one level of quotes surrounding the literal. No error if quotes are not present or are
	 * mixed.
	 */
	public static String deQuote(String literal) {
		int endIdx = literal.length() - 1;
		if (endIdx < 2) return literal;
		char beg = literal.charAt(0);
		char end = literal.charAt(endIdx);
		for (int idx = 0; idx < Q.length; idx++) {
			if (beg == Q[idx][0] && end == Q[idx][1]) {
				return literal.substring(1, endIdx);
			}
		}
		return literal;
	}

	/**
	 * Concats the arguments to produce a valid string URL appropriate for classpath discovery.
	 * Separators are added as needed. All separators are converted to standard separators, ie,
	 * *nix-style.
	 */
	public static String concatAsClassPath(String... args) {
		return concat(args).replaceAll(WINDOWS_SEPARATOR, STD_SEPARATOR);
	}

	/**
	 * Concats the arguments to produce a filesystem path fragment. Separators are added as needed.
	 * All separators are converted to standard separators, ie, *nix-style.
	 */
	public static String concat(String... args) {
		String result = "";
		for (String arg : args) {
			result = FilenameUtils.concat(result, arg);
		}
		return result;
	}

	/**
	 * Converts a standard java package-styled string to a corresponding filesystem path fragment.
	 * */
	public static String convertPkgToPath(String arg) {
		return arg.replace(pkgSep, STD_SEPARATOR);
	}

	/**
	 * Removes any existing extension appended to the name, and then appends the given extension.
	 * Checks to ensure that a dot separator is present between the name and extension.
	 */
	public static String setExtension(String name, String ext) {
		int dot = name.lastIndexOf('.');
		if (dot != -1) {
			name = name.substring(0, dot);
		}
		if (!ext.startsWith(".")) {
			ext = "." + ext;
		}
		return name + ext;
	}

	public static String initialLC(String str) {
		if (str == null) return "";
		if (str.length() < 2) return str.toLowerCase();
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public static String initialUC(String str) {
		if (str == null) return "";
		if (str.length() < 2) return str.toUpperCase();
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String trimLeft(String str) {
		return str.replaceAll("^\\s+", "");
	}

	public static String trimRight(String str) {
		return str.replaceAll("\\s+$", "");
	}

	public static String camelCase(String in) {
		StringBuilder sb = new StringBuilder(in);
		for (int idx = sb.length() - 1; idx >= 0; idx--) {
			char c = sb.charAt(idx);
			if (c == '_') {
				sb.deleteCharAt(idx);
				sb.setCharAt(idx, Character.toUpperCase(sb.charAt(idx)));
			} else if (Character.isUpperCase(c)) {
				sb.setCharAt(idx, Character.toLowerCase(c));
			}
		}
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}

	public static String tokenCase(String in) {
		if (isAllUpperCase(in)) {
			return Character.toString(in.charAt(0)) + in.substring(1).toLowerCase();
		}
		return in;
	}

	public static boolean isAllUpperCase(String in) {
		for (int idx = 0; idx < in.length(); idx++) {
			if (Character.isLowerCase(in.charAt(idx))) {
				return false;
			}
		}
		return true;
	}

	public static String titleCase(String word) {
		if (word == null || word.length() == 0) return "";
		if (word.length() == 1) return word.toUpperCase();
		return word.substring(0, 1).toUpperCase() +
				word.substring(1).toLowerCase();
	}

	/**
	 * Returns the lines of the block prefixed with the indent string. Ensures a leading EOL
	 * sequence and no trailing whitespace.
	 *
	 * @param ci
	 * @param block
	 * @return
	 */
	public static String indentBlock(String ci, String block) {
		if (block == null) return "<Error: indent include block is null>";
		StringReader sr = new StringReader(block);
		BufferedReader buf = new BufferedReader(sr);
		StringBuilder sb = new StringBuilder();
		String s;
		try {
			while ((s = buf.readLine()) != null) {
				sb.append(ci + s + Strings.eol);
			}
			char c = sb.charAt(0);
			if (c != '\n' && c != '\r') {
				sb.insert(0, eol);
			}
			return trimRight(sb.toString());
		} catch (IOException e) {
			sb.append("<Error indenting block: " + e.getMessage() + ">");
		}
		return sb.toString();
	}
}
// StringsClass ==========
