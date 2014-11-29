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
// OpClass ==========
package net.certiv.json.types;

import java.util.HashMap;

public enum Op {
	// maths
	PLUS("+"),
	MINUS("-"),
	MULT("*"),
	DIV("/"),
	MOD("%"),

	// logics (Function\'s suppport)
	EQ("="),
	EQV("=="),
	NEQ("!="),
	LT("<"),
	LTE("<="),
	GT(">"),
	GTE(">=");

	private final String type;
	private static final HashMap<String, Op> types;

	// Note: executed after enum construction.
	// Note: values() is a builtin, hidden iterable of the enums.
	static {
		types = new HashMap<>();
		for (Op op : values()) {
			Op.types.put(op.type(), op);
		}
	}

	Op(String type) {
		this.type = type;
	}

	public String type() {
		return this.type;
	}

	// Note: Enum.valueOf(String) is keyed to the corresponding enum's *name* string.
	public static Op typeOf(String type) {
		Op result = types.get(type);
		if (result != null)
			return result;
		if (type == null)
			throw new NullPointerException("Type is null");
		throw new IllegalArgumentException("No enum constant " + "Op." + type);
	}

	@Override
	public String toString() {
		return this.type;
	}
}

// OpClass ==========
