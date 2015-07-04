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
// TestBulkClass ==========
package net.certiv.json.bulk;

import java.io.File;

import net.certiv.json.util.Log;
import net.certiv.json.util.Log.LogLevel;

public class TestBulk {

	public TestBulk() {}

	private TestIOProcessor processor;

	public static void main(String[] args) {
		new TestBulk(args);
	}

	public TestBulk(String[] args) {

		Log.defLevel(LogLevel.Debug);
		Log.setTestMode(false);

		if (args == null || args.length == 0) {
			String input = "D:/DevFiles/Java/Workspaces/Main/net.certiv.antlr.go/src/org/antlr/v4/runtime";
			args = new String[] { "-i", input, "-t", "java", "-e", "200" };
		}

		Log.info(this, "TestBulk running...");
		processor = new TestIOProcessor(args);
		File root = processor.init();
		if (root != null) {
			Log.info(this, "Startup complete.");

			int beg = processor.getBeg();
			int end = processor.getEnd();
			processor.processFiles(root, beg, end);

			Log.info(this, "Done.");
		} else {
			Log.error(this, "TestBulk Failed.");
		}
	}
}

// TestBulkClass ==========
