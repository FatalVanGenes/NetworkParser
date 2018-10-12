package xml.core.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import xml.core.Utils;

public class Node extends AbstractElement implements Comparable<Node> {

	private static final Set<String> nodeAttributes;

	static {
		nodeAttributes = new HashSet<String>(Arrays.asList(new String[] { "name", "id" }));
	}

	public static Node build(Iterator<?> iterator) {
		Map<String, String> keyValues = Utils.extractKV(iterator, nodeAttributes);
		return new Node(keyValues.get("name"), keyValues.get("id"));
	}

	// attributes
	private final String name;

	private final String id;

	public Node(String name, String id) {
		super("node");
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getSpacer(getRenderIndentLevel()));
		sb.append(String.format("%s { name: %s, id: %s }%n", getTypeName(), getName(), getId()));
		return sb.toString();
	}

	@Override
	public int compareTo(Node o) {
		return name.compareTo(o.getName());
	}
}
