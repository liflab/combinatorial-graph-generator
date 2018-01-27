package ca.uqac.lif.testing.hypergraph;

import java.util.List;

public class VertexListGenerator extends HypergraphGenerator 
{
	public VertexListGenerator(List<String> var_names)
	{
		super(var_names);
	}
	
	@Override
	public void generateTWayEdges(int t)
	{
		System.out.println(getVertexCount());
		super.generateTWayEdges(t);
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

}
