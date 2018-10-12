package xml.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xml.core.Utils;

public class Subnet extends AbstractElement implements Comparable<Subnet>, Hierarchical {

	private static final Set<String> subnetAttributes;

	private static final Set<Class<? extends Element>> childrenClasses;

	static {
		// not loved by the OpenJDK 1.8
		// childrenClasses = new HashSet<>(Arrays.asList(new Class[] { Node.class, Subnet.class }));
		childrenClasses = new HashSet<>();
		childrenClasses.add(Node.class);
		childrenClasses.add(Subnet.class);

		subnetAttributes = new HashSet<>();
		subnetAttributes.add("name");
		subnetAttributes.add("id");
	}

	// attributes
	private final String name;

	private final String id;

	private Subnet parentSubnet;

	// dependents
	private final Set<Node> nodes;

	private final Set<Subnet> subnets;

	public Subnet(String name, String id) {
		super("subnet");
		this.nodes = new HashSet<>();
		this.subnets = new HashSet<>();
		this.name = name;
		this.id = id;
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public Set<Subnet> getSubnets() {
		return subnets;
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
		sb.append(String.format("%s { name: %s, id: %s, parent: %s }%n", getTypeName(), getName(), getId(),
				(parentSubnet != null) ? parentSubnet.getName() : "None"));
		{
			List<Node> sortedNodes = new ArrayList<>(nodes);
			Collections.sort(sortedNodes);
			for (Object node : sortedNodes) {
				sb.append(node);
			}
		}
		{
			List<Subnet> sortedSubnets = new ArrayList<>(subnets);
			Collections.sort(sortedSubnets);
			for (Subnet subnet : sortedSubnets) {
				sb.append(subnet);
			}
		}
		return sb.toString();
	}

	public static Subnet build(Iterator<?> iterator) {
		Map<String, String> keyValues = Utils.extractKV(iterator, subnetAttributes);
		return new Subnet(keyValues.get("name"), keyValues.get("id"));
	}

	@Override
	public void acceptChild(Element element) {
		// System.out.println("adding " + element);
		String typeName = element.getTypeName();
		if ("node".equals(typeName)) {
			getNodes().add((Node) element);
		} else if ("subnet".equals(typeName)) {
			getSubnets().add((Subnet) element);
		} else {
			throw new RuntimeException("logic error here");
		}
	}

	@Override
	public boolean acceptsChildType(Element element) {
		return childrenClasses.contains(element.getClass());
	}

	@Override
	public int compareTo(Subnet o) {
		return name.compareTo(o.getName());
	}

	public Subnet getParentSubnet() {
		return parentSubnet;
	}

	public void setParentSubnet(Subnet parentSubnet) {
		this.parentSubnet = parentSubnet;
	}

	public void bindParents() {
		for (Subnet subnet : subnets) {
			subnet.setParentSubnet(this);
			subnet.bindParents();
		}
	}
}
