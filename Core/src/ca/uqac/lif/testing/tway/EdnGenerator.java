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
    m_output.println("{");
    super.generateTWayEdges();
    m_output.println();
    m_output.println("}");
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
    m_output.print("\"" + m_edgeNb + "\" #{");
    m_edgeNb++;
    m_firstVertex = true;
  }

  @Override
  public void edgeEnd()
  {
    m_output.print("}");
    m_edgeCount++;
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
      m_output.print(" ");
    }
    m_output.print(getVertexId(vertex));
  }

}
