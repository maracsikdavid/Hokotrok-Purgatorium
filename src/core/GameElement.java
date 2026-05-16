package core;

public interface GameElement {
    void accept(GameElementVisitor visitor, String id);
}