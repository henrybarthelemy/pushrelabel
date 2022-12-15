public class Edge {
  int capacity; // max flow / capacity of edge
  int flow; //current flow
  int u, v; //Edge u -> v

  Edge(int flow, int capacity, int u, int v) {
    this.flow = flow;
    this.capacity = capacity;
    this.u = u;
    this.v = v;
  }
}
