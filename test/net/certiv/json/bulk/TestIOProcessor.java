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
// TestIOProcessorClass ==========
package net.certiv.json.bulk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import net.certiv.json.parser.JsonErrorListener;
import net.certiv.json.parser.JsonTokenFactory;
import net.certiv.json.parser.gen.JsonLexer;
import net.certiv.json.parser.gen.JsonParser;
import net.certiv.json.parser.gen.JsonParser.JsonContext;
import net.certiv.json.util.Log;
import net.certiv.json.util.Log.LogLevel;

public class TestIOProcessor {

	private String[] opts; // pathnames
	private String source; // processed pathnames
	private String target;

	private ArrayList<String> types = new ArrayList<>();

	private boolean FileIn; // flags
	private boolean FileOut;
	private boolean StdIO;
	private boolean TextIn;

	private String srcData; // loaded source data
	private int beg = 0;
	private int end = 100;

	public TestIOProcessor(String[] args) {
		Log.setLevel(this, LogLevel.Debug);
		if (args.length == 0) {
			printHelp();
		}
		opts = new String[3];
		opts[0] = opts[1] = opts[2] = "";
		for (int idx = 0; idx < args.length; idx++) {

			switch (args[idx].trim().toLowerCase()) {
				case "-b":
					idx++;
					beg = Integer.parseInt(args[idx]);
					break;
				case "-e":
					idx++;
					end = Integer.parseInt(args[idx]);
					break;
				case "-t":
					idx++;
					types.add(args[idx]);
					break;
				case "-i":
					idx++;
					source = args[idx];
					break;
				case "-h":
				default:
					printHelp();
					break;
			}
		}
	}

	public File init() {
		if (source == null) {
			Log.error(this, "Need to specify an input source.");
			return null;
		}

		File root = new File(source);
		if (!root.exists()) {
			Log.error(this, "Source file does not exist (" + source + ")");
			return null;
		}

		return root;
	}

	public String getSourceName() {
		return source;
	}

	public String loadData() {
		if (FileIn) {
			String msg = "Error reading source data from file '" + source + "'";
			try {
				srcData = FileUtils.readFileToString(new File(source));
			} catch (IOException e) {
				Log.error(this, msg);
			}
		} else if (StdIO && !TextIn) {
			String msg = "Error reading source data from standard in";
			InputStream in = System.in;
			try {
				srcData = IOUtils.toString(System.in);
			} catch (IOException e) {
				Log.error(this, msg);
			} finally {
				IOUtils.closeQuietly(in);
			}
		} else if (TextIn) {
			StdIO = true;
		}
		return srcData;
	}

	public void storeData(String data) {
		if (FileOut) {
			try {
				FileUtils.writeStringToFile(new File(target), data);
			} catch (IOException e) {
				Log.error(this, "Error writing result data to file '" + target + "'", e);
			}
		} else if (StdIO) {
			OutputStream out = System.out;
			try {
				IOUtils.write(data, out);
			} catch (IOException e) {
				Log.error(this, "Error writing result data to standard out", e);
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
	}

	public void processFiles(File root, int begin, int end) {
		int count = 0;
		int passed = 0;
		if (root.isFile() && end > 0) {
			evaluateFile(root);
		} else if (root.isDirectory()) {
			String[] checkedTypes = getCheckedTypes();
			for (File file : FileUtils.listFiles(root, checkedTypes, true)) {
				count++;
				if (count >= begin) {
					Log.debug(this, "Processing (" + count + ") " + file.getPath());
					passed += evaluateFile(file);
				} else {
					Log.debug(this, "Skipping (" + count + ") " + file.getPath());
				}
				if (count >= end) break;
			}
		}
		Log.info(this, "Processed " + count + "/" + passed + " files/passed.");

	}

	private int evaluateFile(File file) {

		String lastError = "Failure in acquiring input stream.";
		JsonContext result = null;
		try {
			ANTLRFileStream input = new ANTLRFileStream(file.getPath());
			input.name = file.getName();

			lastError = "Failure in generating lexer token stream.";
			JsonLexer lexer = new JsonLexer(input);
			JsonTokenFactory factory = new JsonTokenFactory(input);
			lexer.setTokenFactory(factory);
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			lastError = "Failure in parser production.";
			JsonParser parser = new JsonParser(tokens);
			parser.setTokenFactory(factory);
			parser.removeErrorListeners(); // remove ConsoleErrorListener
			parser.addErrorListener(new JsonErrorListener());
			// parser.addErrorListener(new DiagnosticErrorListener());
			// parser.setErrorHandler(new JsonParserErrorStrategy());
			result = parser.json();
		} catch (RecognitionException | IOException e) {
			Log.error(this, file.getName() + ": " + lastError);
			return 0;
		}
		if (result != null && result.getChildCount() > 0) {
			return 1;
		}
		return 0;
	}

	private String[] getCheckedTypes() {
		int num = types.size();
		return types.toArray(new String[num]);
	}

	public int getBeg() {
		return beg;
	}

	public int getEnd() {
		return end;
	}

	@SuppressWarnings("unused")
	private String cwd() {
		String cwd = "";
		try {
			cwd = new File(".").getCanonicalPath();
		} catch (IOException e) {}
		return cwd;
	}

	@SuppressWarnings("unused")
	private String readFile(String filename) {
		File file = new File(filename);
		String data = null;
		try {
			data = FileUtils.readFileToString(file);
		} catch (IOException e) {
			Log.error(this, "Error reading file", e);
		}
		return data;

	}

	private void printHelp() {
		System.out.println("Usage:");
		System.out.println("java -jar [cli_options]" + System.lineSeparator());
		// etc.
		System.exit(0);
	}
}

// TestIOProcessorClass ==========
