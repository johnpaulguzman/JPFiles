package Degen;

import java.lang.reflect.Field;

//Export as Runnable Jar File to repack external dependencies
import com.cedarsoftware.util.io.JsonWriter;

public class Debugger {
	private static boolean isTerminatable(Class<?> type) {
		return (type.isPrimitive() && type != void.class) || type == String.class || type.isEnum()
				|| type == Double.class || type == Float.class || type == Long.class || type == Integer.class
				|| type == Short.class || type == Character.class || type == Byte.class || type == Boolean.class;
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

	public static String dump(StringBuilder sb, Object obj) {
		if (obj == null) {
			return "null";
		}
		if (sb == null) {
			sb = new StringBuilder();
		}

		sb.append("{");
		if (isTerminatable(obj.getClass())) {
			sb.append(String.format("\"(%s)\": \"%s\", ", obj.getClass(), String.valueOf(obj)));
		} else {
			sb.append(String.format("\"(%s)\": {", obj.getClass()));
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object nestedObj = attemptGetObject(field, obj);
				if (isTerminatable(field.getType())) {
					sb.append(String.format("\"(%s) %s\": \"%s\", ", field.getType(), field.getName(),
							String.valueOf(nestedObj)));
				} else if (Iterable.class.isAssignableFrom(field.getType())) {
					sb.append(String.format("\"(%s) %s\": [", field.getType(), field.getName()));
					for (Object o : (Iterable<?>) nestedObj) {
						dump(sb, o);
					}
					sb.append("], ");
				} else {
					sb.append(String.format("\"(%s) %s\": [", field.getType(), field.getName()));
					dump(sb, nestedObj);
					sb.append("], ");
				}
			}
			sb.append("}, ");
		}
		sb.append("}, ");
		return sb.toString().replaceAll(", }", "}").replaceAll(", ]", "]");
	}

	public static String prettyDump(Object obj) {
		String pprint = JsonWriter.formatJson(dump(null, obj));
		System.out.println(pprint);
		return JsonWriter.formatJson(pprint);
	}
}
