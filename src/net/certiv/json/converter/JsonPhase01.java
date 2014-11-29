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
// Phase01Class ==========
package net.certiv.json.converter;

import java.util.ArrayList;

import net.certiv.json.converter.descriptors.JsonDescriptor;
import net.certiv.json.generator.IOProcessor;
import net.certiv.json.parser.gen.JsonParser.JsonContext;
import net.certiv.json.util.Log;
import net.certiv.json.util.Reflect;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class JsonPhase01 extends JsonPhaseBase {

	public static final int phase = 01;
	public boolean collectComments;

	/**
	 * Phase Plan: <br>
	 * -- create typed descriptor objects for context instances <br>
	 * -- if enabled, examine and collect comments from hidden-channel <br>
	 * -- initialize descriptors <br>
	 * -- where the descriptor is contextually complete, mark the descriptor 'resolved' <br>
	 * 
	 * @param processor
	 */
	public JsonPhase01(PhaseState state, IOProcessor processor) {
		super(state, processor);
		this.state.nodeContextMap = new ParseTreeProperty<BaseDescriptor>();
		this.state.commentMarkers = new ArrayList<Integer>();
		Log.info(this, "Phase " + phase + ": Starting...");
	}

	@Override
	public void exitJson(JsonContext ctx) {
		super.exitJson(ctx);
		JsonDescriptor descriptor = (JsonDescriptor) getDescriptor(ctx);
		Log.info(this, "Phase " + phase + ": completed (resolved: " + descriptor.resolved + ")");
	}

	public void collectComments(boolean collect) {
		this.collectComments = collect;
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		super.enterEveryRule(ctx);

		String className = ctx.getClass().getName();
		int pathIdx = className.indexOf("parser.gen");
		className = className.substring(0, pathIdx) + "converter.descriptors.";
		int nameIdx = ctx.getClass().getSimpleName().lastIndexOf("Context");
		className = className + ctx.getClass().getSimpleName().substring(0, nameIdx) + "Descriptor";
		Object[] args = { ctx };
		BaseDescriptor descriptor;
		try {
			descriptor = (BaseDescriptor) Reflect.make(Class.forName(className), args);
		} catch (ClassNotFoundException e) {
			Log.error(this, "Failed to make " + className, e);
			return;
		}
		descriptor.setPhaseState(state);
		setDescriptor(ctx, descriptor);

		if (collectComments) {
			descriptor.collectComments = collectComments;
			descriptor.commentLeft = commentLeft(ctx);
			descriptor.commentRight = commentRight(ctx);
		}

		Log.info(this, "Phase " + phase + ": Created " + className);
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		super.exitEveryRule(ctx);
		BaseDescriptor descriptor = getDescriptor(ctx);
		descriptor.initialize();

		Log.info(this, "Phase " + phase + ": Initialized (resolved: " + descriptor.resolved + ")");
	}
}

// Phase01Class ==========
