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
// ValueTypeClass ==========
package net.certiv.json.types;

/** The type of the asString contained by a descriptor */
public enum ValueType {
	//
	EXECUTABLE("Executable", StyleType.ACTION),
	OP("Op", StyleType.ACTION),
	//
	COMMAND("Command", StyleType.COMMAND),
	DEFINE("Define", StyleType.COMMAND),
	DEFER("Defer", StyleType.COMMAND),
	INCLUDE("Include", StyleType.COMMAND),
	REPEAT("Repeat", StyleType.COMMAND),
	UNKNOWN("Unknown", StyleType.COMMAND),
	//
	ELEMENT("Element", StyleType.TAG),
	CLASS("Class", StyleType.TAG),
	ID("Id", StyleType.TAG),
	ATTR("Attr", StyleType.TAG),
	SELECTOR("Selector", StyleType.TAG),
	//
	CONTENT("Content", StyleType.CONTENT),
	TEXT("Text", StyleType.CONTENT),
	TEMPLATE("Template", StyleType.CONTENT),
	//
	NUMBER("Number", StyleType.LITERAL),
	NAME("Name", StyleType.LITERAL),
	LITERAL("Literal", StyleType.LITERAL),
	//
	STYLEVAR("StyleVar", StyleType.VARIABLE),
	VARIABLE("Variable", StyleType.VARIABLE),
	ASSIGN("Assign", StyleType.VARIABLE),
	CONCAT("Concat", StyleType.VARIABLE);

	private String text;
	private StyleType type;

	ValueType(String text, StyleType type) {
		this.text = text;
		this.type = type;
	}

	public String text() {
		return text;
	}

	public StyleType type() {
		return type;
	}

	public String toString() {
		return text;
	}
}

// ValueTypeClass ==========
