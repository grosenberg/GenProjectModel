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
 * // Description ==========
 * Part of GenPackage
 * // Description ==========
 *
 * Versions:
 * // Version ==========
 * 		1.0 - 2014.03.26: First release level code
 * 		1.1 - 2014.08.26: Updates, add Tests support
 * // Version ==========
 *******************************************************************************/
// JsonMainClass ==========
package net.certiv.json;

import net.certiv.json.converter.Converter;
import net.certiv.json.converter.PhaseState;
import net.certiv.json.util.Log;

public class JsonMain {

	private IOProcessor processor;

	public static void main(String[] args) {
		new JsonMain(args);
	}

	public JsonMain(String[] args) {

		if (args == null || args.length == 0) {
			String input = "D:/DevFiles/Java/WorkSpaces/Main/GenProjectModel/Sample.json";
			args = new String[] { "-i", input };
		}

		processor = new IOProcessor(args);
		if (processor.init()) {
			Log.info(this, "JsonMain running...");

			String srcData = processor.loadData();
			if (srcData.length() == 0) {
				Log.error(this, "Startup failed.");
				return;
			}
			Log.info(this, "Startup complete.");

			Converter remarkConverter = new Converter(processor);
			String result = remarkConverter.convert(srcData);
			processor.storeData(result);

			Log.info(this, "Done.");
		} else {
			Log.error(this, "JsonMain Failed.");
		}
	}

	/**
	 * JsonMain - re-entry point for embedded use<br>
	 * <br>
	 * Usage:<br>
	 * JsonMain jm = new JsonMain();<br>
	 * String resultData = jm.start(String srcData, SymbolTable symTable);
	 */
	public JsonMain() {
		super();
	}

	/**
	 * Start embedded run
	 * 
	 * @param srcData data to evaluate
	 * @param state carry data between phases - inlcudes symbol table
	 * @param iop the I/O processor
	 * @return
	 */
	public String start(String srcData, PhaseState state, IOProcessor iop) {
		Converter nominal = new Converter(iop);
		PhaseState srcState = state.clone();
		return nominal.convert(srcData, srcState);
	}
}

// JsonMainClass ==========
