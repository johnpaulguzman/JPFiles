package Degen;

import java.lang.reflect.Field;

//Export as Runnable Jar File to repack external dependencies
//import com.cedarsoftware.util.io.JsonWriter;

public class Debugger {
	private static boolean isTerminatable(Class<?> type) {
		return (type.isPrimitive() && type != void.class) || (type == String.class || type == char[].class)
				|| type.isEnum() || type == Double.class || type == Float.class || type == Long.class
				|| type == Integer.class || type == Short.class || type == Character.class || type == Byte.class
				|| type == Boolean.class;
	}

	private static Object attemptGetObject(Field field, Object obj) {
		Object nestedObj = null;
		try {
			nestedObj = field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// e.printStackTrace();
		}
		return nestedObj;
	}

	public static void dump(Object obj) {
		String pprint = dump(new StringBuilder(), "", obj);
		System.out.println(pprint);
		// return pprint;
	}

	public static String dump(StringBuilder sb, String indent, Object obj) { // do recursive call right after for field
		obj = (obj == null) ? "(java.lang.Void) null" : obj;

		sb.append(indent + "{" + "\n");
		//indent += "\t";
		if (isTerminatable(obj.getClass())) {
			sb.append(indent + String.format("\"(%s)\": \"%s\", ", obj.getClass(), String.valueOf(obj)) + "\n");
		} else {
			sb.append(indent + String.format("\"(%s)\": {", obj.getClass()) + "\n");
			//indent += "\t";
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object nestedObj = attemptGetObject(field, obj);
				if (isTerminatable(field.getType())) {
					sb.append(indent + String.format("\"(%s) %s\": \"%s\", ", field.getType(), field.getName(),
							String.valueOf(nestedObj)) + "\n");
				} else if (Iterable.class.isAssignableFrom(field.getType())) {
					sb.append(indent + String.format("\"(%s) %s\": [", field.getType(), field.getName()) + "\n");
					for (Object o : (Iterable<?>) nestedObj) {
						dump(sb, indent + "\t", o);
					}
					sb.append(indent + "], " + "\n");
				} else {
					sb.append(indent + String.format("\"(%s) %s\": [", field.getType(), field.getName()) + "\n");
					dump(sb, indent + "\t", nestedObj);
					sb.append(indent + "], " + "\n");
				}
			}
			sb.append(indent + "}, " + "\n");
		}
		sb.append(indent + "}, " + "\n");
		return sb.toString()
				.replaceAll(", \n}", "\n}").replaceAll(", \n]", "\n]")
				.replaceAll(", \n\t}", "\n\t}").replaceAll(", \n\t]", "\n\t]")
				.replaceAll(", \n\t\t}", "\n\t\t}").replaceAll(", \n\t\t]", "\n\t\t]");
	}

	// public static String prettyDump(Object obj) {
	// String pprint = JsonWriter.formatJson(dump(null, obj));
	// System.out.println(pprint);
	// return JsonWriter.formatJson(pprint);
	// }
}
