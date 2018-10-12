package xml.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xml.core.Utils;

public class Network extends AbstractElement implements Hierarchical {

	private static final Set<String> networkAttributes;

	private static final Set<Class<? extends Element>> childrenClasses;

	static {
		childrenClasses = new HashSet<>(
//				Arrays.asList(new Class[] { Subnet.class, Node.class })
				);
		childrenClasses.add(Node.class);
		childrenClasses.add(Subnet.class);

		networkAttributes = new HashSet<>(Arrays.asList(new String[] { "name", "id" }));
	}

	// attributes
	private final String name;

	private final String id;

	// dependents
	private final Set<Demand> demands;

	private final Set<Node> nodes;

	private final Set<Link> links;

	private final Set<Path> paths;

	private final Set<Profile> profiles;

	private final Set<Subnet> subnets;

	public Network(String name, String id) {
		super("network");
		this.demands = new HashSet<>();
		this.links = new HashSet<>();
		this.nodes = new HashSet<>();
		this.paths = new HashSet<>();
		this.profiles = new HashSet<>();
		this.subnets = new HashSet<>();
		this.name = name;
		this.id = id;
	}

	public Set<Demand> getDemands() {
		return demands;
	}

	public Set<Link> getLinks() {
		return links;
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public Set<Path> getPaths() {
		return paths;
	}

	public Set<Profile> getProfiles() {
		return profiles;
	}

	public Set<Subnet> getSubnets() {
		return subnets;
	}

	public static Network build(Iterator<?> iterator) {
		Map<String, String> keyValues = Utils.extractKV(iterator, networkAttributes);
		return new Network(keyValues.get("name"), keyValues.get("id"));
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	@Override
	public boolean acceptsChildType(Element element) {
		return childrenClasses.contains(element.getClass());
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
			throw new RuntimeException("double plus bad");
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getSpacer(getRenderIndentLevel()));
		sb.append(String.format("%s { name: %s, id: %s }%n", getTypeName(), getName(), getId()));
		{
			List<Subnet> sortedSubnets = new ArrayList<>(subnets);
			Collections.sort(sortedSubnets);
			for (Subnet subnet : sortedSubnets) {
				sb.append(subnet);
			}
		}
		{
			List<Node> sortedNodes = new ArrayList<>(nodes);
			Collections.sort(sortedNodes);
			for (Object node : sortedNodes) {
				sb.append(node);
			}
		}
		return sb.toString();
	}
	
	public void postProcess() {
		for (Subnet subnet : subnets) {
			subnet.bindParents();
		}
	}
}
