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
// ScopeClass ==========
package net.certiv.json.symbol;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import net.certiv.json.util.Strings;

public class Scope {

	public final int genId;

	public ScopeType type;
	public Scope enclosingScope;
	protected Map<String, Symbol> symbolMap = new LinkedHashMap<String, Symbol>();

	public Scope(ScopeType type, final int genId, Scope enclosingScope) {
		this.type = type;
		this.genId = genId;
		this.enclosingScope = enclosingScope;
	}

	/** Define a symbol in the current scope */
	public void define(Symbol symbol) {
		symbol.setScope(this);
		symbolMap.put(symbol.name, symbol);
	}

	/**
	 * Look up the symbol name in this scope and, if not found, progressively search the enclosing
	 * scopes. Return null if not found in any applicable scope.
	 */
	public Symbol resolve(String name) {
		Symbol symbol = symbolMap.get(name);
		if (symbol != null) return symbol;
		if (enclosingScope != null) return enclosingScope.resolve(name);
		return null; // not found
	}

	public Symbol resolve(String name, ArrayList<String> parameters) {
		String params = Strings.asString(parameters, true, ".");
		return resolve(name + params);
	}

	/** Where to look next for symbols */
	public Scope enclosingScope() {
		return enclosingScope;
	}

	public String toString() {
		return symbolMap.keySet().toString();
	}
}
// ScopeClass ==========
