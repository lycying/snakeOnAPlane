package info.u250.snakeonaplane.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Compress {

	public static void main(String[] argv) throws IOException {
		FileOutputStream out = new FileOutputStream("levels/level.bin");
		for (int i=1;i<28;i++) {
			String name = "levels/"+i;
			System.out.println(name);
			byte data[] = new byte[30*20];
			FileInputStream in = new FileInputStream(name);
			in.read(data);
			in.close();
			out.write(data);
		}
		out.close();
	}
}
