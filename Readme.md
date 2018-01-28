Generates a Graphviz file representing a graph or hypergraph from
a t-way test coverage problem.

```
Usage: java -jar hypergraph-generator.jar [--help] [-t x] [--method m] <filename>
  -t x       Generates conditions for n-way test coverage
  --method m Use method m (coloring, hypergraph, edn)
  filename   File containing the domains for each variable and the conditions
```