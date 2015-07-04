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
// Phase03Class ==========
package net.certiv.json.converter;

import net.certiv.json.IOProcessor;
import net.certiv.json.symbol.ScopeType;
import net.certiv.json.util.Log;
import net.certiv.json.util.Reflect;

// Phase03Class ==========

// Phase03Imports ==========
// generated imports
// Phase03Imports ==========

import net.certiv.json.converter.descriptors.ArrayDescriptor;
import net.certiv.json.converter.descriptors.ElementDescriptor;
import net.certiv.json.converter.descriptors.JsonDescriptor;
import net.certiv.json.converter.descriptors.ObjectDescriptor;
import net.certiv.json.converter.descriptors.ValueDescriptor;
import net.certiv.json.parser.gen.JsonParser.ArrayContext;
import net.certiv.json.parser.gen.JsonParser.ElementContext;
import net.certiv.json.parser.gen.JsonParser.JsonContext;
import net.certiv.json.parser.gen.JsonParser.ObjectContext;
import net.certiv.json.parser.gen.JsonParser.ValueContext;

// Phase03Body ==========

public class JsonPhase03 extends JsonPhaseBase {

	public static final int phase = 03;
	public static boolean statusResolved = true;

	private int startingGen;

	/**
	 * Phase Plan: <br>
	 * -- build a symbol (substitution) table for expressions <br>
	 * -- symbols are scoped local to each body <br>
	 * -- process the descriptors to resolve dynamic values <br>
	 * -- unresolved nodes are reported as errors.<br>
	 * 
	 * @param processor
	 */
	public JsonPhase03(JsonPhaseBase prior, IOProcessor processor) {
		super(prior.state, processor);
		this.startingGen = prior.state.symTable.getCurrentGen();
	}

	public boolean balanceCheck() {
		if (state.symTable.currentScope().genId != startingGen) {
			Log.warn(this, "Unbalanced scopes! Start: " + startingGen + ", Ended: " + state.symTable.getCurrentGen());
			if (startingGen == 0) {
				if (state.symTable.currentScope().type != ScopeType.GLOBAL) {
					Log.warn(this, "Did not end at GLOBAL.");
				}
			}
			return false;
		}
		return true;
	}

	// Phase03Body ==========

	// Phase03Methods ==========
	// generated methods
	// Phase03Methods ==========

	@Override
	public void enterElement(ElementContext ctx) {
		super.enterElement(ctx);
		state.symTable.pushScope();
		Log.debug(this, "Phase " + phase + ": New local scope");
		ElementDescriptor descriptor = (ElementDescriptor) getDescriptor(ctx);
		descriptor.processOnEntry();
	}

	@Override
	public void enterJson(JsonContext ctx) {
		super.enterJson(ctx);
		state.symTable.pushScope();
		Log.debug(this, "Phase " + phase + ": New local scope");
		JsonDescriptor descriptor = (JsonDescriptor) getDescriptor(ctx);
		descriptor.processOnEntry();
	}

	@Override
	public void enterValue(ValueContext ctx) {
		super.enterValue(ctx);
		state.symTable.pushScope();
		Log.debug(this, "Phase " + phase + ": New local scope");
		ValueDescriptor descriptor = (ValueDescriptor) getDescriptor(ctx);
		descriptor.processOnEntry();
	}

	@Override
	public void enterObject(ObjectContext ctx) {
		super.enterObject(ctx);
		state.symTable.pushScope();
		Log.debug(this, "Phase " + phase + ": New local scope");
		ObjectDescriptor descriptor = (ObjectDescriptor) getDescriptor(ctx);
		descriptor.processOnEntry();
	}

	// Phase03MethodSet ==========

	@Override
	public void enterArray(ArrayContext ctx) {
		super.enterArray(ctx);
		state.symTable.pushScope();
		Log.debug(this, "Phase " + phase + ": New local scope");
		ArrayDescriptor descriptor = (ArrayDescriptor) getDescriptor(ctx);
		descriptor.processOnEntry();
	}

	@Override
	public void exitArray(ArrayContext ctx) {
		super.exitArray(ctx);
		ArrayDescriptor descriptor = (ArrayDescriptor) getDescriptor(ctx);
		descriptor.processOnExit();
		state.symTable.popScope();

		String name = Reflect.simpleClassName(descriptor);
		Log.debug(this, "Phase " + phase + ": " + name + " processed (resolved: " + descriptor.resolved + ")");
	}

	// Phase03MethodSet ==========

	@Override
	public void exitElement(ElementContext ctx) {
		super.exitElement(ctx);
		ElementDescriptor descriptor = (ElementDescriptor) getDescriptor(ctx);
		descriptor.processOnExit();
		state.symTable.popScope();

		String name = Reflect.simpleClassName(descriptor);
		Log.debug(this, "Phase " + phase + ": " + name + " processed (resolved: " + descriptor.resolved + ")");
	}

	@Override
	public void exitJson(JsonContext ctx) {
		super.exitJson(ctx);
		JsonDescriptor descriptor = (JsonDescriptor) getDescriptor(ctx);
		descriptor.processOnExit();
		state.symTable.popScope();

		String name = Reflect.simpleClassName(descriptor);
		Log.debug(this, "Phase " + phase + ": " + name + " processed (resolved: " + descriptor.resolved + ")");
	}

	@Override
	public void exitValue(ValueContext ctx) {
		super.exitValue(ctx);
		ValueDescriptor descriptor = (ValueDescriptor) getDescriptor(ctx);
		descriptor.processOnExit();
		state.symTable.popScope();

		String name = Reflect.simpleClassName(descriptor);
		Log.debug(this, "Phase " + phase + ": " + name + " processed (resolved: " + descriptor.resolved + ")");
	}

	@Override
	public void exitObject(ObjectContext ctx) {
		super.exitObject(ctx);
		ObjectDescriptor descriptor = (ObjectDescriptor) getDescriptor(ctx);
		descriptor.processOnExit();
		state.symTable.popScope();

		String name = Reflect.simpleClassName(descriptor);
		Log.debug(this, "Phase " + phase + ": " + name + " processed (resolved: " + descriptor.resolved + ")");
	}

	// Phase03End ==========

}

// Phase03End ==========
