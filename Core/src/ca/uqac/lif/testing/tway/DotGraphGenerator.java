package ca.uqac.lif.testing.tway;

import java.util.List;

public class DotGraphGenerator extends GraphGenerator
{
	public DotGraphGenerator(int t, List<String> var_names)
	{
		super(t, var_names);
	}
	
	@Override
	public void generateTWayEdges()
	{
		m_output.println("graph G {");
		super.generateTWayEdges();
		m_output.println("}");
	}

	@Override
	public void vertexCallback(long src_id, List<String> tuple1, int tuple_nb1, List<String> values1, long dst_id, List<String> tuple2, int tuple_nb2, List<String> values2)
	{
		m_output.println(src_id + " -- " + dst_id + ";");
	}
}
