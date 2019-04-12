/*
  Copyright 2015
  Laboratoire d'informatique formelle
  Université du Québec à Chicoutimi, Canada
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
      http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
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
  public void vertexCallback(long src_id, List<String> tuple1, int tuple_nb1, List<String> values1,
      long dst_id, List<String> tuple2, int tuple_nb2, List<String> values2)
  {
    m_output.println(src_id + " -- " + dst_id + ";");
  }
}
