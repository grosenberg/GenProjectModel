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

import net.certiv.json.IOProcessor;
import net.certiv.json.parser.gen.JsonLexer;
import net.certiv.json.parser.gen.JsonParserBaseListener;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

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
	 * Search left for comments. Stop on first non-comment, previously indexed comment, or BOF. The index of the first
	 * new comment token is recorded.
	 */
	public String commentLeft(ParserRuleContext rc) {
		int dot = rc.getStart().getTokenIndex();
		if (dot <= 0) return "";
		int idx = dot;
		int prev = dot - 1;
		boolean onlyWS = true;
		boolean done = false;
		while (!done) {
			switch (this.state.tokens.get(prev).getType()) {
				case JsonLexer.Comment:
				case JsonLexer.CommentLine:
					if (this.state.commentMarkers.contains(prev)) {
						done = true;
						break;
					}
					onlyWS = false;
					idx = prev;
					this.state.commentMarkers.add(idx);
				case JsonLexer.HorzWS:
				case JsonLexer.VertWS:
					if (prev > 0) {
						prev--;
						break;
					}
				default:
					done = true;
			}
		}
		if (onlyWS) return "";

		StringBuilder sb = new StringBuilder();
		for (; idx < dot; idx++) {
			sb.append(this.state.tokens.get(idx).getText());
		}
		return sb.toString();
	}

	/**
	 * Search right for comments. Stop on first VWS or non-Comment token or EOF. The index of the first comment token is
	 * recorded.
	 */
	public String commentRight(ParserRuleContext rc) {
		int dot = rc.getStop().getTokenIndex();
		int idx = dot;
		int mark = dot;
		int next = dot + 1;
		boolean onlyWS = true;
		boolean done = false;
		while (!done) {
			switch (this.state.tokens.get(next).getType()) {
				case JsonLexer.Comment:
				case JsonLexer.CommentLine:
					if (this.state.commentMarkers.contains(next)) {
						done = true;
						break;
					}
					onlyWS = false;
					if (idx == dot) idx = next;
					mark = next;
					this.state.commentMarkers.add(idx);
				case JsonLexer.HorzWS:
					next++;
					break;
				case JsonLexer.VertWS:
					mark = next;
				case JsonLexer.EOF:
				default:
					done = true;
			}
		}
		if (onlyWS) return "";

		StringBuilder sb = new StringBuilder();
		for (dot++; dot <= mark; dot++) {
			sb.append(this.state.tokens.get(dot).getText());
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
