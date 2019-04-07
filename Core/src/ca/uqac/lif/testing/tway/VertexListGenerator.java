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
    m_output.println(getVertexCount());
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
    m_output.println();
    m_edgeCount++;
  }

  @Override
  public void vertexCallback(List<String> vertex)
  {
    m_output.print(getVertexId(vertex));
    m_output.print(" ");
  }

}
