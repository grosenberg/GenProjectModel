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

import net.certiv.json.generator.IOProcessor;
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

	public String commentLeft(ParserRuleContext rc) {
		int tdx = rc.getStart().getTokenIndex();
		if (tdx <= 0) return "";
		int jdx = tdx - 1;
		boolean onlyWS = true;
		boolean done = false;
		while (!done) {
			switch (this.state.tokens.get(jdx).getType()) {
				case JsonLexer.Comment:
				case JsonLexer.CommentLine:
					onlyWS = false;
				case JsonLexer.HorzWS:
				case JsonLexer.VertWS:
					if (jdx > 0) {
						jdx--;
					} else {
						done = true;
					}
					break;
				default:
					done = true;
			}
		}
		if (onlyWS) return "";
		if (this.state.commentMarkers.contains(jdx)) {
			return "";
		} else {
			this.state.commentMarkers.add(jdx);
		}

		StringBuilder sb = new StringBuilder();
		for (; jdx < tdx; jdx++) {
			sb.append(this.state.tokens.get(jdx).getText());
		}
		return sb.toString();
	}

	public String commentRight(ParserRuleContext rc) {
		int tdx = rc.getStop().getTokenIndex();
		int jdx = tdx + 1;
		boolean onlyWS = true;
		boolean done = false;
		while (!done) {
			switch (this.state.tokens.get(jdx).getType()) {
				case JsonLexer.CommentLine:
					onlyWS = false;
				case JsonLexer.HorzWS:
					jdx++;
					break;
				case JsonLexer.EOF:
					done = true;
					break;
				default:
					done = true;
			}
		}
		if (onlyWS) return "";
		if (this.state.commentMarkers.contains(jdx)) {
			return "";
		} else {
			this.state.commentMarkers.add(jdx);
		}

		StringBuilder sb = new StringBuilder();
		for (tdx++; tdx <= jdx; tdx++) {
			sb.append(this.state.tokens.get(tdx).getText());
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
