package xml.core.model;

public interface Hierarchical {

	void acceptChild(Element element);

	boolean acceptsChildType(Element element);

	default void addChild(Element element) {
		if (acceptsChildType(element)) {
			acceptChild(element);
		}
	}
}
