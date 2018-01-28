package ca.uqac.lif.testing.tway;

import java.util.List;

public class VertexListGenerator extends HypergraphGenerator 
{
	public VertexListGenerator(int t, List<String> var_names)
	{
		super(t, var_names);
	}
	
	@Override
	public void generateTWayEdges()
	{
		System.out.println(getVertexCount());
		super.generateTWayEdges();
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
