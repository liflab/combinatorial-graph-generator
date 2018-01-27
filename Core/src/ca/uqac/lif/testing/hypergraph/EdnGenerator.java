package ca.uqac.lif.testing.hypergraph;

import java.util.List;

public class EdnGenerator extends HypergraphGenerator 
{
	protected boolean m_firstEdge = true;
	
	protected boolean m_firstVertex = true;
	
	protected long m_edgeNb = 0;
	
	public EdnGenerator(List<String> var_names)
	{
		super(var_names);
	}
	
	@Override
	public void generateTWayEdges(int t)
	{
		System.out.println("{");
		super.generateTWayEdges(t);
		System.out.println();
		System.out.println("}");
	}

	@Override
	public void edgeStart()
	{
		if (m_firstEdge)
		{
			m_firstEdge = false;
		}
		else
		{
			System.out.println(",");
		}
		System.out.print("\"" + m_edgeNb + "\" #{");
		m_edgeNb++;
		m_firstVertex = true;
	}

	@Override
	public void edgeEnd()
	{
		System.out.print("}");
	}

	@Override
	public void vertexCallback(List<String> vertex)
	{
		if (m_firstVertex)
		{
			m_firstVertex = false;
		}
		else
		{
			System.out.print(" ");
		}
		System.out.print(getVertexId(vertex));
	}

}
