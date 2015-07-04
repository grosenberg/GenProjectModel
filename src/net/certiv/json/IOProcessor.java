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
// TestIOProcessor ==========
package net.certiv.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import net.certiv.json.util.Log;
import net.certiv.json.util.Log.LogLevel;

public class IOProcessor {

	private String source; // dir pathnames
	private String target;

	private ArrayList<String> types = new ArrayList<>();

	public IOProcessor(String[] args) {
		Log.setLevel(this, LogLevel.Debug);
		if (args.length == 0) {
			printHelp();
		}

		for (int idx = 0; idx < args.length; idx++) {

			switch (args[idx].trim().toLowerCase()) {
				case "-i":
					idx++;
					source = args[idx];
					break;
				case "-o":
					idx++;
					target = args[idx];
					break;
				case "-l":
					idx++;
					Log.defLevel(args[idx].trim());
					break;
				case "-t":
					idx++;
					types.add(args[idx]);
					break;

				case "-h":
				default:
					printHelp();
					break;
			}
		}
	}

	private void printHelp() {
		String levels = "'debug', 'info', 'warn', 'error', or 'silent' (default: 'warn'";
		System.out.println("Usage:");
		System.out.println("java -jar [cli_options]" + System.lineSeparator());
		System.out.println("-h                print cli help");
		System.out.println("-i <dir>          root directory for source files");
		System.out.println("-l <string>       set logging level to string: " + levels);
		System.out.println("-o <dir>          root directory for output files");
		System.out.println("-t <types>        types of input files to convert");
		System.exit(0);
	}

	public boolean init() {
		if (source == null) {
			Log.error(this, "Need to specify a source directory path.");
			return false;
		}
		if (target == null) {
			Log.error(this, "Need to specify a destination directory path.");
			return false;
		}

		File root = new File(source);
		if (!root.exists()) {
			Log.error(this, "Source directory does not exist (" + source + ")");
			return false;
		}

		// TODO: handle clear and force options
		// root = new File(target);
		// if (root.exists()) {
		// root.delete();
		// }

		return true;
	}

	public String getSourceDir() {
		return source;
	}

	public String getTargetDir() {
		return target;
	}

	// TODO: convert based on type
	public String convertName(String pName) {
		String name = pName.replaceFirst("Java/src", "Json/src");
		int dot = name.lastIndexOf("java");
		name = name.substring(0, dot) + "json";
		return name;
	}

	public Collection<File> collectSourceFiles(File root) {
		String[] checkedTypes = getCheckedTypes();
		Collection<File> files = FileUtils.listFiles(root, checkedTypes, true);
		return files;
	}

	private String[] getCheckedTypes() {
		int num = types.size();
		return types.toArray(new String[num]);
	}

	public String loadData(File source) {
		String srcData = "";
		try {
			srcData = FileUtils.readFileToString(source);
		} catch (IOException e) {
			Log.error(this, "Error reading source data from file '" + source + "'");
		}
		return srcData;
	}

	public void storeData(String destination, String data) {
		try {
			FileUtils.writeStringToFile(new File(destination), data);
		} catch (IOException e) {
			Log.error(this, "Error writing result data to file '" + target + "'", e);
		}
	}

	@SuppressWarnings("unused")
	private String cwd() {
		String cwd = "";
		try {
			cwd = new File(".").getCanonicalPath();
		} catch (IOException e) {}
		return cwd;
	}
}
// TestIOProcessor ==========
