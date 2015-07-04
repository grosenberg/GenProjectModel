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
// PhaseBaseClass ==========
package net.certiv.json.converter;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import net.certiv.json.IOProcessor;
import net.certiv.json.parser.gen.JsonLexer;
import net.certiv.json.parser.gen.JsonParserBaseListener;

public class JsonPhaseBase extends JsonParserBaseListener {

	protected PhaseState state;
	protected IOProcessor processor;

	/**
	 * Constructor with phase transference
	 */
	public JsonPhaseBase(PhaseState state, IOProcessor processor) {
		super();
		this.state = state;
		this.processor = processor;
	}

	public PhaseState getState() {
		return state;
	}

	public BaseDescriptor getDescriptor(ParseTree ctx) {
		return this.state.nodeContextMap.get(ctx);
	}

	public void setDescriptor(ParseTree ctx, BaseDescriptor ncd) {
		this.state.nodeContextMap.put(ctx, ncd);
	}

	public String childText(ParserRuleContext rc, int idx) {
		return rc.getChild(idx).getText();
	}

	/**
	 * Search left for comments. Stop on first non-comment, previously indexed comment, or
	 * BOF. The index of the first new comment token is recorded.
	 */
	public String commentLeft(ParserRuleContext rc) {
		int dot = rc.getStart().getTokenIndex();
		if (dot < 0) return "";
		int mark = dot;
		boolean done = false;
		while (!done) {
			switch (state.tokens.get(mark - 1).getType()) {
				case JsonLexer.Comment:
				case JsonLexer.CommentLine:
				case JsonLexer.HorzWS:
				case JsonLexer.VertWS:
					mark--;
					if (mark <= 0) done = true;
					break;

				default:
					done = true;
			}
		}

		if (state.commentMarkers.contains(mark)) return "";
		state.commentMarkers.add(mark);

		StringBuilder sb = new StringBuilder();
		for (; mark < dot; mark++) {
			sb.append(state.tokens.get(mark).getText());
		}
		return sb.toString();
	}

	/**
	 * Search right for comments. Stop on first VWS or non-Comment token or EOF. The index
	 * of the first comment token is recorded.
	 */
	public String commentRight(ParserRuleContext rc) {
		int dot = rc.getStop().getTokenIndex();
		int end = state.tokens.size() - 1;
		if (dot > end) return "";
		int mark = dot;
		boolean done = false;
		while (!done) {
			switch (state.tokens.get(mark + 1).getType()) {
				case JsonLexer.Comment:
				case JsonLexer.CommentLine:
				case JsonLexer.HorzWS:
				case JsonLexer.VertWS:
					mark++;
					if (mark >= end) done = true;
					break;

				default:
					done = true;
			}
		}

		if (mark == dot) return "";
		if (mark > dot && state.commentMarkers.contains(dot + 1)) return "";
		state.commentMarkers.add(dot + 1);

		StringBuilder sb = new StringBuilder();
		for (dot++; dot <= mark; dot++) {
			sb.append(state.tokens.get(dot).getText());
		}
		return sb.toString();
	}

	/**
	 * Returns the qualified name after removal of the last 'dotted' segment.
	 * 
	 * @param qualifiedName
	 * @return
	 */
	public String removeTerminal(String qualifiedName) {
		int idx = qualifiedName.lastIndexOf('.');
		if (idx == -1) return qualifiedName;
		return qualifiedName.substring(0, idx);
	}

	public ParseTree nextPeer(ParserRuleContext ctx, int idx) {
		List<ParseTree> ctxs = ctx.children;
		if (idx + 1 < ctxs.size()) {
			return ctxs.get(idx + 1);
		}
		return null;
	}
}

// PhaseBaseClass ==========
