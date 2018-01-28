package ca.uqac.lif.testing.tway;

import java.io.File;
import java.io.FileNotFoundException;
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
		parser.addArgument(new Argument().withShortName("t").withArgument("x").withDescription("Generates hyperedges for t=x"));
		parser.addArgument(new Argument().withLongName("method").withArgument("m").withDescription("Generates graph for method m (coloring, edn, hypergraph)"));
		parser.addArgument(new Argument().withLongName("help").withDescription("Show command line usage"));
		ArgumentMap arguments = parser.parse(args);
		if (arguments.hasOption("help"))
		{
			parser.printHelp("Hypergraph generator", System.out);
			System.exit(0);
		}
		if (!arguments.hasOption("t"))
		{
			System.err.println("Missing parameter t");
			System.exit(2);
		}
		if (!arguments.hasOption("method"))
		{
			System.err.println("Missing parameter method");
			System.exit(2);
		}
		int t = Integer.parseInt(arguments.getOptionValue("t"));
		String method = arguments.getOptionValue("method");
		String filename = arguments.getOthers().get(0);
		TWayGraphProblem twp = null;
		Map<String,List<String>> domains = null;
		try
		{
			domains = parseDomains(new File(filename));

		}
		catch (FileNotFoundException e)
		{
			System.err.println("File " + filename + " not found");
			System.exit(1);
		}
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
		else
		{
			System.err.println("Unknown method " + method);
			System.exit(2);
		}
		twp.addDomain(domains);
		twp.generateTWayEdges();
	}
	
	protected static Map<String,List<String>> parseDomains(File f) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(f);
		List<String> var_names = new ArrayList<String>();
		Map<String,List<String>> domains = new HashMap<String,List<String>>();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
				continue;
			String[] parts = line.split(":");
			String var_name = parts[0].trim();
			var_names.add(var_name);
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
