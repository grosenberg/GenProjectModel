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
package net.certiv.json.converter.descriptors;

import net.certiv.json.converter.BaseDescriptor;
import net.certiv.json.converter.Value;
import net.certiv.json.parser.gen.JsonParser.ArrayContext;
import net.certiv.json.parser.gen.JsonParser.ObjectContext;
import net.certiv.json.parser.gen.JsonParser.ValueContext;

import org.antlr.v4.runtime.tree.TerminalNode;

public class ValueDescriptor extends BaseDescriptor {

	public ValueDescriptor(ValueContext ctx) {
		super(ctx);
	}

	@Override
	public String content(boolean enter) {
		StringBuilder sb = new StringBuilder();
		if (enter) {
			sb.append(getLeftComment());
		} else {
			sb.append(getRightComment());
		}
		return sb.toString();
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

	public TerminalNode String() {
		return context().String();
	}

	public ObjectContext object() {
		return context().object();
	}

	public TerminalNode False() {
		return context().False();
	}

	public TerminalNode Number() {
		return context().Number();
	}

	public TerminalNode Null() {
		return context().Null();
	}

	public TerminalNode True() {
		return context().True();
	}

	public ArrayContext array() {
		return context().array();
	}

	public ValueContext context() {
		return (ValueContext) ctx;
	}
}
