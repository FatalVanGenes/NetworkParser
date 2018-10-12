package xml.core;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.events.Attribute;

public class Utils {

	private static final Map<Integer, String> spacers = new HashMap<>();

	private Utils() {
		// TODO Auto-generated constructor stub
	}

	public static String getSpacer(int indent) {
		if (!spacers.containsKey(indent)) {
			spacers.put(indent, CharBuffer.allocate(indent).toString().replace('\0', '\t'));
		}
		return spacers.get(indent);
	}

	public static Map<String, String> extractKV(Iterator<?> keyValue, Set<String> keyNames) {
		Map<String, String> keyValues = new HashMap<>();
		while (keyValue.hasNext()) {
			Attribute o = (Attribute) keyValue.next();
			String name = o.getName().getLocalPart();
			String value = o.getValue();
			if (keyNames.contains(name)) {
				keyValues.put(name, value);
			}
		}
		return keyValues;
	}
}
