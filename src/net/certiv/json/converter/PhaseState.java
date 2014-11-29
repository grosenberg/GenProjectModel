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
// PhaseStateClass ==========
package net.certiv.json.converter;

import java.util.ArrayList;
import java.util.Stack;

import net.certiv.json.symbol.SymbolTable;
import net.certiv.json.util.Log;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class PhaseState {

	// shared data among all descriptors
	public SymbolTable symTable;
	public CommonTokenStream tokens; // the reference token stream
	public ParseTreeProperty<BaseDescriptor> nodeContextMap;
	public ArrayList<Integer> commentMarkers;
	public StringBuilder doc; // output buffer

	private final Stack<PhaseState> stack;

	public PhaseState() {
		stack = new Stack<>();
		symTable = new SymbolTable();
		doc = new StringBuilder();
	}

	private PhaseState(PhaseState state) {
		stack = state.stack;
		symTable = state.symTable.clone();
		doc = new StringBuilder();
	}

	public PhaseState save() {
		stack.push(this);
		return new PhaseState(this);
	}

	public PhaseState restore() {
		if (depth() < 1) {
			Log.error(this, "No further PhaseState to restore.");
			return this;
		}
		return stack.pop();
	}

	public int depth() {
		return stack.size();
	}

	public PhaseState clone() {
		return new PhaseState(this);
	}
}

// PhaseStateClass ==========
