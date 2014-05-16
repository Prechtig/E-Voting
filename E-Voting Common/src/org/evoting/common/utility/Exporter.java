package org.evoting.common.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;

public class Exporter {
	// write one method to save a given string to a given filename. A static class to handle that maybe? to load and save files
	public static void exportElGamalPublicKeyParameters(ElGamalPublicKeyParameters pubK, String fileName) throws IOException {
		BigInteger y = pubK.getY();
		ElGamalParameters param = pubK.getParameters();
		BigInteger g = param.getG();
		BigInteger p = param.getP();

		StringBuilder sb = new StringBuilder();
		sb.append("y:");
		sb.append(y);
		sb.append(System.getProperty("line.separator"));
		sb.append("g:");
		sb.append(g);
		sb.append(System.getProperty("line.separator"));
		sb.append("p:");
		sb.append(p);
		sb.append(System.getProperty("line.separator"));

		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(sb.toString());
		bw.close();

	}

	public static void exportElGamalPrivateKeyParameters(ElGamalPrivateKeyParameters privK, String fileName) throws IOException {
		BigInteger x = privK.getX();
		ElGamalParameters param = privK.getParameters();
		BigInteger g = param.getG();
		BigInteger p = param.getP();

		StringBuilder sb = new StringBuilder();
		sb.append("x:");
		sb.append(x);
		sb.append(System.getProperty("line.separator"));
		sb.append("g:");
		sb.append(g);
		sb.append(System.getProperty("line.separator"));
		sb.append("p:");
		sb.append(p);
		sb.append(System.getProperty("line.separator"));

		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(sb.toString());
		bw.close();

	}

	public static void exportRsaKey(String pathname, Key key) throws IOException {
		Files.write(Paths.get(pathname), key.getEncoded(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
}
