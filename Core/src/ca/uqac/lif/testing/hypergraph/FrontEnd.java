package ca.uqac.lif.testing.hypergraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ca.uqac.lif.testing.hypergraph.CliParser.Argument;
import ca.uqac.lif.testing.hypergraph.CliParser.ArgumentMap;

public class FrontEnd 
{
	public static void main(String[] args)
	{
		CliParser parser = new CliParser();
		parser.addArgument(new Argument().withShortName("t").withArgument("x").withDescription("Generates hyperedges for t=x"));
		parser.addArgument(new Argument().withLongName("edn").withDescription("Generates graph in EDN format"));
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
		int t = Integer.parseInt(arguments.getOptionValue("t"));
		String filename = arguments.getOthers().get(0);
		try
		{
			HypergraphGenerator psg = createGenerator(new File(filename), arguments.hasOption("edn"));
			psg.generateTWayEdges(t);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("File " + filename + " not found");
			System.exit(1);
		}
	}

	protected static HypergraphGenerator createGenerator(File f, boolean edn) throws FileNotFoundException
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
		HypergraphGenerator psg;
		if (edn)
		{
			psg = new EdnGenerator(var_names);
		}
		else
		{
			psg = new VertexListGenerator(var_names);
		}
		for (Map.Entry<String,List<String>> dom : domains.entrySet())
		{
			psg.addDomain(dom.getKey(), dom.getValue());
		}
		return psg;
	}

}
