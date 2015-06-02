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

import java.util.List;

import net.certiv.json.converter.BaseDescriptor;
import net.certiv.json.converter.Value;
import net.certiv.json.parser.gen.JsonParser.ElementContext;
import net.certiv.json.parser.gen.JsonParser.ObjectContext;

import org.antlr.v4.runtime.tree.TerminalNode;

public class ObjectDescriptor extends BaseDescriptor {

	public ObjectDescriptor(ObjectContext ctx) {
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

	public List<TerminalNode> Sep() {
		return context().Sep();
	}

	public List<ElementContext> element() {
		return context().element();
	}

	public TerminalNode CBrace() {
		return context().CBrace();
	}

	public TerminalNode OBrace() {
		return context().OBrace();
	}

	public ObjectContext context() {
		return (ObjectContext) ctx;
	}
}
