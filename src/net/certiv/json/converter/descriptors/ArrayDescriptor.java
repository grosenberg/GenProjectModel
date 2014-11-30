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
// DescriptorClass ==========
package net.certiv.json.converter.descriptors;

import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;
import net.certiv.json.converter.BaseDescriptor;
import net.certiv.json.converter.Value;

// DescriptorClass ==========

// ImportNamedContexts ==========
// Generated imports 
// ImportNamedContexts ==========

import net.certiv.json.parser.gen.JsonParser.ArrayContext;
import net.certiv.json.parser.gen.JsonParser.ValueContext;

// DescriptorBody ==========

public class ArrayDescriptor extends BaseDescriptor {

	public ArrayDescriptor(ArrayContext ctx) {
		super(ctx);
	}

	@Override
	public void initialize() {
		value = Value.TRUE;
	}

	@Override
	public Value process() {
		if (!resolved) {
			resolved = true;
		}
		return value;
	}

	// DescriptorBody ==========

	// AccessMethods ==========
	// Generated access methods
	// AccessMethods ==========

	public ValueContext f() {
		return context().f;
	}

	public List<ValueContext> g() {
		if (context().g != null && context().g.size() == 0) return null;
		return context().g;
	}

	public List<TerminalNode> Sep() {
		if (context().Sep() != null && context().Sep().size() == 0) return null;
		return context().Sep();
	}

	public List<ValueContext> value() {
		if (context().value() != null && context().value().size() == 0) return null;
		return context().value();
	}

	public TerminalNode OBracket() {
		return context().OBracket();
	}

	public TerminalNode CBracket() {
		return context().CBracket();
	}

	// DescriptorEnd ==========

	public ArrayContext context() {
		return (ArrayContext) ctx;
	}
}

// DescriptorEnd ==========
