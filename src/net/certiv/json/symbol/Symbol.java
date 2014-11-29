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
// SymbolClass ==========
package net.certiv.json.symbol;

import net.certiv.json.converter.BaseDescriptor;
import net.certiv.json.types.ValueType;

public class Symbol {

	protected Scope scope; // the owning scope
	protected BaseDescriptor descriptor;
	protected String name;
	protected ValueType type;

	public Symbol(BaseDescriptor descriptor, String name, ValueType type) {
		this.descriptor = descriptor;
		this.name = name;
		this.type = type;
	}

	public BaseDescriptor getDescriptor() {
		return descriptor;
	}

	public String getName() {
		return name;
	}

	public ValueType getType() {
		return type;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Scope getScope() {
		return scope;
	}

	public int genId() {
		return scope.genId;
	}

	public String toString() {
		if (type != null) return '<' + getName() + ":" + type + '>';
		return getName();
	}

	public Symbol clone() {
		return new Symbol(this.descriptor, this.name, this.type);
	}
}
// SymbolClass ==========
