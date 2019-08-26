/*
  Copyright 2015
  Laboratoire d'informatique formelle
  Université du Québec à Chicoutimi, Canada

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ca.uqac.lif.testing.tway;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ca.uqac.lif.testing.tway.CliParser.Argument;
import ca.uqac.lif.testing.tway.CliParser.ArgumentMap;

/**
 * Command-line front-end to the graph generator
 */
public class FrontEnd
{
	public static void main(String[] args)
	{
		CliParser parser = new CliParser();
		parser.addArgument(
				new Argument().withShortName("t").withArgument("x").withDescription("Generates for t=x"));
		parser.addArgument(new Argument().withShortName("n").withArgument("x")
				.withDescription("If no input_file : Generates for n=x"));
		parser.addArgument(new Argument().withShortName("v").withArgument("x")
				.withDescription("If no input_file : Generates for v=x"));
		parser.addArgument(new Argument().withLongName("method").withArgument("m").withDescription(
				"Generates graph for method m (variables (if -n and -v), coloring, edn, hypergraph)"));
		parser.addArgument(new Argument().withShortName("o").withArgument("f")
				.withDescription("Output graph to file f"));
		parser.addArgument(
				new Argument().withLongName("help").withDescription("Show command line usage"));
		ArgumentMap arguments = parser.parse(args);

		if (arguments.hasOption("help"))
		{
			parser.printHelp(
					"Graph / Hypergraph generator\njava -jar <jar_name> [OPTIONS] input_file\n\nOPTIONS:",
					System.out);
			System.exit(0);
		}

		if (!arguments.hasOption("method"))
		{
			System.err.println("Missing parameter method");
			System.exit(2);
		}
		String method = arguments.getOptionValue("method");

		if (!arguments.hasOption("t") && !method.equals("variables"))
		{
			System.err.println("Missing parameter t");
			System.exit(2);
		}

		PrintStream out = System.out;
		if (arguments.hasOption("o"))
		{
			String out_filename = arguments.getOptionValue("o");
			try
			{
				// We set true so that the print stream auto-flushes
				out = new PrintStream(new FileOutputStream(new File(out_filename)), true);
			}
			catch (FileNotFoundException e)
			{
				System.err.println("File not open: " + out_filename);
				System.exit(2);
			}
		}

		String filename = "";
		List<String> others = arguments.getOthers();
		if (others.size() > 0)
			filename = others.get(0);

		TWayGraphProblem twp = null;
		Map<String, List<String>> domains = null;

		try
		{
			domains = parseDomains(new File(filename));

		}
		catch (FileNotFoundException e)
		{
			if (arguments.hasOption("n") && arguments.hasOption("v"))
			{
				int n = Integer.parseInt(arguments.getOptionValue("n"));
				int v = Integer.parseInt(arguments.getOptionValue("v"));
				domains = createDomains(n, v);
			}
			else
			{
				System.err.println("File " + filename + " not found");
				System.exit(1);
			}

		}

		if (method.equalsIgnoreCase("variables"))
		{
			for (Map.Entry<String, List<String>> var : domains.entrySet())
			{
				out.print(var.getKey() + ':');
				List<String> vals = var.getValue();
				for (int i = 0; i < vals.size(); i++)
				{
					out.print(vals.get(i));
					if (i != vals.size() - 1)
						out.print(',');
				}
				out.print('\n');
			}
			System.exit(0);
		}

		List<String> var_names = new ArrayList<String>(domains.size());
		var_names.addAll(domains.keySet());
		int t = Integer.parseInt(arguments.getOptionValue("t"));

		if (method.equalsIgnoreCase("coloring"))
		{
			twp = new DotGraphGenerator(t, var_names);
		}
		else if (method.equalsIgnoreCase("hypergraph"))
		{
			twp = new VertexListGenerator(t, var_names);
		}
		else if (method.equalsIgnoreCase("edn"))
		{
			twp = new EdnGenerator(t, var_names);
		}
		else
		{
			System.err.println("Unknown method " + method);
			System.exit(2);
		}

		twp.setOutput(out);
		twp.addDomain(domains);
		twp.generateTWayEdges();

		if (out != null)
			out.close();
	}

	public static Map<String, List<String>> createDomains(int n, int v)
	{
		Map<String, List<String>> domains = new HashMap<String, List<String>>();
		for (Integer n_iter = 0; n_iter < n; n_iter++)
		{
			List<String> values = new ArrayList<String>(v);
			for (Integer v_iter = 0; v_iter < v; v_iter++)
			{
				values.add(v_iter.toString());
			}
			domains.put("var" + n_iter.toString(), values);
		}
		return domains;
	}

	public static TWayGraphProblem getGenerator(int t, String method, String in_filename, PrintStream out) throws IOException
	{
		TWayGraphProblem twp = null;
		Map<String,List<String>> domains;
		domains = parseDomains(new File(in_filename));
		List<String> var_names = new ArrayList<String>(domains.size());
		var_names.addAll(domains.keySet());
		if (method.equalsIgnoreCase("coloring"))
		{
			twp = new DotGraphGenerator(t, var_names);
		}
		else if (method.equalsIgnoreCase("hypergraph"))
		{
			twp = new VertexListGenerator(t, var_names);
		}
		else if (method.equalsIgnoreCase("edn"))
		{
			twp = new EdnGenerator(t, var_names);
		}
		if (out != null)
		{
			twp.setOutput(out);
		}
		twp.addDomain(domains);
		return twp;
	}

	public static Map<String, List<String>> parseDomains(File f) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(f);
		Map<String, List<String>> domains = new HashMap<String, List<String>>();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
				continue;
			String[] parts = line.split(":");
			String var_name = parts[0].trim();
			String[] vals = parts[1].split(",");
			List<String> values = new ArrayList<String>(vals.length);
			for (String v : vals)
			{
				values.add(v.trim());
			}
			domains.put(var_name, values);
		}
		scanner.close();
		return domains;
	}

}
