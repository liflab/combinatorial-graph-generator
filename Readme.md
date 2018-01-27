Generates a Graphviz file representing the hypergraph whose vertex
covering creates a t-way test coverage.

Usage: java -jar hypergraph-generator.jar [--help] [-t x] <filename>
  -t x      Generates conditions for n-way test coverage
  --edn     Output in EDN format (used for hitting-set)
  filename  File containing the domains for each variable and the conditions
