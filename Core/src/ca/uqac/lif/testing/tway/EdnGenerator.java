package ca.uqac.lif.testing.tway;

import java.util.List;

/**
 * Generates a hypergraph in the EDN file format (used by the
 * <tt>hitting-set</tt> tool)  
 */
public class EdnGenerator extends HypergraphGenerator 
{
	protected boolean m_firstEdge = true;
	
	protected boolean m_firstVertex = true;
	
	protected long m_edgeNb = 0;
	
	public EdnGenerator(int t, List<String> var_names)
	{
		super(t, var_names);
	}
	
	@Override
	public void generateTWayEdges()
	{
		System.out.println("{");
		super.generateTWayEdges();
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
