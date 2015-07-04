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

import java.io.File;

import net.certiv.json.converter.Converter;
import net.certiv.json.converter.PhaseState;
import net.certiv.json.util.Log;

public class JsonMain {

	public static void main(String[] args) {
		new JsonMain(args);
	}

	public JsonMain(String[] args) {

		if (args == null || args.length == 0) {
			String input = "D:/DevFiles/Java/WorkSpaces/Main/net.certiv.json/Json/src/samples";
			args = new String[] { "-i", input, "-t", "java" };
		}

		Log.info(this, "Java2Go running...");
		IOProcessor processor = new IOProcessor(args);
		if (processor.init()) {
			File root = new File(processor.getSourceDir());
			Converter converter = new Converter(processor);

			for (File file : processor.collectSourceFiles(root)) {
				String srcName = file.getPath();
				srcName = srcName.replaceAll("\\\\", "/");
				String srcData = processor.loadData(file);

				if (srcData.length() == 0) {
					Log.error(this, "Skipping " + srcName);
					continue;
				}

				String outName = processor.convertName(srcName);
				String result = converter.convert(srcName, srcData);
				processor.storeData(outName, result);
			}

			Log.info(this, "Done.");
		} else {
			Log.error(this, "Failed.");
		}
	}

	/**
	 * JsonMain - re-entry point for embedded use<br>
	 * <br>
	 * Usage:<br>
	 * JsonMain jm = new JsonMain();<br>
	 * String resultData = jm.start(String srcName, String srcData, SymbolTable symTable);
	 */
	public JsonMain() {
		super();
	}

	/**
	 * Start embedded run
	 * 
	 * @param srcName file name associated with srcData
	 * @param srcData data to evaluate
	 * @param state carry data between phases - inlcudes symbol table
	 * @param iop the I/O processor
	 * @return
	 */
	public String start(String srcName, String srcData, PhaseState state, IOProcessor iop) {
		Converter nominal = new Converter(iop);
		PhaseState srcState = state.clone();
		return nominal.convert(srcName, srcData, srcState);
	}
}

// JsonMainClass ==========
