package Degen;

import java.util.ArrayList;
import java.util.List;

public class TestingPlayground {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		new TestingPlayground().runTest();
	}
	
	public void runTest() {
		TestA a = new TestA();
		System.out.println(Debugger.prettyDump(a));
		System.out.println(Debugger.dump(null, a));
		System.out.println(Debugger.dump(new StringBuilder(), true));
		System.out.println(Debugger.dump(new StringBuilder(), null));
	}

	enum C {
		o, p, q
	}

	class TestA {
		private String a;
		private int b;
		private List<String> c;
		private TestB z;
		private C v;

		public TestA() {
			a = null;
			b = 2;
			c = new ArrayList<String>();
			c.add("e");
			c.add("f");
			z = new TestB();
			v = C.o;
		}
	}

	class TestB {
		private String d;
		private int e;
		private List<String> f;

		public TestB() {
			d = "9";
			e = 8;
			f = new ArrayList<String>();
			f.add("x");
			f.add("y");
		}
	}
}
