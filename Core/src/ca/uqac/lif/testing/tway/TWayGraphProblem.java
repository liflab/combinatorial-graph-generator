package ca.uqac.lif.testing.tway;

import java.util.List;

public abstract class TWayGraphProblem extends TWayProblem
{
	public TWayGraphProblem(int t, String ... var_names)
	{
		super(t, var_names);
	}
	
	public TWayGraphProblem(int t, List<String> var_names)
	{
		super(t, var_names);
	}
	
	/**
	 * Generates the set of hyperedges corresponding to the <i>t</i>-way
	 * test case generation problem.
	 */
	public abstract void generateTWayEdges();
	
	/**
	 * Method called when the generator starts a new hyperedge
	 */
	public void edgeStart()
	{
		// Do nothing
	}
	
	/**
	 * Method called when the generator ends a hyperedge
	 */
	public void edgeEnd()
	{
		// Do nothing
	}
}
