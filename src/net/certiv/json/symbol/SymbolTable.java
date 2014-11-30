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
// SymbolTableClass ==========
package net.certiv.json.symbol;

import java.util.ArrayList;
import java.util.Stack;

import net.certiv.json.util.Log;

public class SymbolTable {

	protected Stack<Scope> scopeStack;
	protected ArrayList<Scope> allScopes;
	protected int genId = 0;

	public SymbolTable() {
		init();
	}

	protected void init() {
		scopeStack = new Stack<>();
		allScopes = new ArrayList<>();
		genId = 0;

		Scope globals = new Scope(ScopeType.GLOBAL, nextGenId(), null);
		scopeStack.push(globals);
		allScopes.add(globals);
	}

	public Scope pushScope() {
		Scope enclosingScope = scopeStack.peek();
		Scope scope = new Scope(ScopeType.LOCAL, nextGenId(), enclosingScope);
		scopeStack.push(scope);
		allScopes.add(scope);
		return scope;
	}

	public void popScope() {
		scopeStack.pop();
	}

	public Scope currentScope() {
		if (scopeStack.size() > 0) {
			return scopeStack.peek();
		}
		Log.error(this, "Unbalanced scope stack.");
		return allScopes.get(0);
	}

	public Scope getScope(int genId) {
		for (Scope scope : scopeStack) {
			if (scope.genId == genId) return scope;
		}
		return null;
	}

	public int getCurrentGen() {
		return genId;
	}

	private int nextGenId() {
		genId++;
		return genId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Scope scope : scopeStack.subList(0, scopeStack.size() - 1)) {
			sb.append(scope.toString());
		}
		return sb.toString();
	}

	@Override
	public SymbolTable clone() {
		SymbolTable nst = new SymbolTable();
		for (Scope scope : scopeStack) {
			if (scope.type == ScopeType.GLOBAL) continue;
			for (String name : scope.symbolMap.keySet()) {
				Symbol sym = scope.resolve(name).clone();
				nst.currentScope().define(sym);
			}
			nst.pushScope();
		}
		return nst;
	}
}
// SymbolTableClass ==========
