/*******************************************************************************
 * // Copyright ==========
 * Copyright (c) 2008-2015 G Rosenberg.
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
// JsonClass ==========
package net.certiv.json.converter;

import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import net.certiv.json.util.Log;
import net.certiv.json.util.Strings;

public class Json {

	private static final String templateDir = "net/certiv/json/converter/templates";
	private static final STGroupFile jsonGen = new STGroupFile(Strings.concatAsClassPath(templateDir, "Json.stg"));

	private Json() {}

	public static String gen(String name) {
		return gen(name, null);
	}

	public static String gen(String name, Map<String, Object> varMap) {
		Log.debug(Json.class, name);
		ST st = jsonGen.getInstanceOf(name);
		if (varMap != null) {
			for (String varName : varMap.keySet()) {
				try {
					st.add(varName, varMap.get(varName));
				} catch (NullPointerException e) {
					Log.error(Json.class, "Error adding attribute: "
							+ name + ":" + varName + " [" + e.getMessage() + "]");
				}
			}
		}
		return st.render();
	}
}

// JsonClass ==========
