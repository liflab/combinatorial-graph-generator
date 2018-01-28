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
 * Command-line front-end to the hypergraph generator
 */
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
			HypergraphGenerator psg = createGenerator(t, new File(filename), arguments.hasOption("edn"));
			psg.generateTWayEdges();
		}
		catch (FileNotFoundException e)
		{
			System.err.println("File " + filename + " not found");
			System.exit(1);
		}
	}

	/**
	 * Parses a file to instantiate a hypergraph generator
	 * @param f The file to read from
	 * @param edn Whether to instantiate an EDN generator
	 * @return The graph generator
	 * @throws FileNotFoundException If the file was not found
	 */
	protected static HypergraphGenerator createGenerator(int t, File f, boolean edn) throws FileNotFoundException
	{
		
		HypergraphGenerator psg;
		if (edn)
		{
			psg = new EdnGenerator(t, var_names);
		}
		else
		{
			psg = new VertexListGenerator(t, var_names);
		}
		for (Map.Entry<String,List<String>> dom : domains.entrySet())
		{
			psg.addDomain(dom.getKey(), dom.getValue());
		}
		return psg;
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
