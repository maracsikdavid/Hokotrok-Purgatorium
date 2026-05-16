package core;

public interface GameElementVisitor {
    void visit(entities.Vehicle vehicle, String id);
    void visit(topology.Lane lane, String id);
    void visit(topology.Road road, String id);
    void visit(topology.MapNode node, String id);
    void visit(actors.Player player, String id);
}