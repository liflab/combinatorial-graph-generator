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

public class PrintStreamGenerator extends HypergraphGenerator 
{
	public PrintStreamGenerator(List<String> var_names)
	{
		super(var_names);
	}

	public static void main(String[] args)
	{
		CliParser parser = new CliParser();
		parser.addArgument(new Argument().withShortName("t").withArgument("x").withDescription("Generates hyperedges for t=x"));
		ArgumentMap arguments = parser.parse(args);
		if (!arguments.hasOption("t"))
		{
			System.err.println("Missing parameter t");
			System.exit(2);
		}
		int t = Integer.parseInt(arguments.getOptionValue("t"));
		String filename = arguments.getOthers().get(0);
		try
		{
			PrintStreamGenerator psg = createGenerator(new File(filename));
			System.out.println(psg.getVertexCount());
			psg.generateTWayEdges(t);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("File " + filename + " not found");
			System.exit(1);
		}
	}

	@Override
	public void edgeStart()
	{
		// Do nothing
	}

	@Override
	public void edgeEnd()
	{
		System.out.println();
	}

	@Override
	public void vertexCallback(List<String> vertex)
	{
		System.out.print(getVertexId(vertex));
		System.out.print(" ");
	}

	protected static PrintStreamGenerator createGenerator(File f) throws FileNotFoundException
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
		PrintStreamGenerator psg = new PrintStreamGenerator(var_names);
		for (Map.Entry<String,List<String>> dom : domains.entrySet())
		{
			psg.addDomain(dom.getKey(), dom.getValue());
		}
		return psg;
	}
}
