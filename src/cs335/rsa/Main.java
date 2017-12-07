package cs335.rsa;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class Main {

	public static void main(String[] args) {
		DemoRSA();
		DecryptHW();
		TimeCrackRSA();
	}

	// Time RSA cracker over increasing key length.
	public static void TimeCrackRSA() {
		int keyLength = 14;	   // RSA key length (bits)
		long timeCrack = 0;    // time to crack (ms)
		double timeCrackSec=0; // time to crack (seconds)
		int avg = 10;          // number of attempts to average over
		long timeStart;		   // start time of crack attempt (ms)
		long timeTotal;		   // total time of all attempts (ms)
		String statsFile = "projectFiles/crackTimes.txt";
		String statsString = "Key Length (bits)\tTime (ms)\tTime (sec)\n";
		BigIntegerRSA rsa;
		BigIntegerRSA crackedRSA;
		// increase key length until crack takes more than one minute:
		while (timeCrack < 60000 && keyLength < 70) {
			timeCrack = 0;
			timeTotal = 0;
			// average time over several attempts:
			for (int i=0; i<avg; i++) {
				rsa = new BigIntegerRSA(keyLength);
				timeStart = System.currentTimeMillis();
				crackedRSA = new BigIntegerRSA(rsa.PublicKey());
				timeTotal += System.currentTimeMillis() - timeStart;
			}
			timeCrack = timeTotal / avg;
			// save stats to statsFile:
			timeCrackSec = (double) timeCrack * 0.001;
			statsString += "" + keyLength + "\t" + timeCrack + "\t" + 
				String.format("%.3f", timeCrackSec) + "\n";
			// print stats to screen:
			System.out.println("keyLength = " + keyLength);
			System.out.println("     time = " + timeCrack +"ms");
			keyLength++;
		}
		// write statsFile:
		try {
			PrintWriter outFile = new PrintWriter(statsFile);
			outFile.print(statsString);
			outFile.close();
			System.out.println("> Stats written: " + statsFile);
		} catch (IOException e) {
			System.out.println("> Problem writing: " + statsFile);
			e.printStackTrace();
		}
	}

	// Decrypt the provided message from assignment.
	public static void DecryptHW() {
		BigInteger N = new BigInteger("608485549753");
		BigInteger E = new BigInteger("7");
		BigInteger[] publicKey = new BigInteger[2];
		publicKey[0] = N;
		publicKey[1] = E;
		BigInteger[] eMessage = new BigInteger[8];
		eMessage[0] = new BigInteger("576322461849");
		eMessage[1] = new BigInteger("122442824098");
		eMessage[2] = new BigInteger("34359738368");
		eMessage[3] = new BigInteger("29647771149");
		eMessage[4] = new BigInteger("140835578744");
		eMessage[5] = new BigInteger("546448062804");
		eMessage[6] = new BigInteger("120078454173");
		eMessage[7] = new BigInteger("42618442977");
		BigIntegerRSA crack = new BigIntegerRSA(publicKey);
		System.out.println(crack.Decrypt(eMessage));
	}
	
	// Demo method.
	public static void DemoRSA() {
		// generate random keys of length 32 bits:
		System.out.println("Random 32 bit key:");
		BigIntegerRSA rsaRandom32 = new BigIntegerRSA(32);
		rsaRandom32.PrintInformation();
		// build keys from specific primes P and Q:
		System.out.println();
		System.out.println("Key from prime P=43 and Q=59:");
		BigIntegerRSA rsaPQ = new BigIntegerRSA(43,59);
		rsaPQ.PrintInformation();
		// build keys from primes P, Q and encryption exponent E:
		System.out.println();
		System.out.println("Key from P=43, Q=59, and E=11:");
		BigIntegerRSA rsaPQE = new BigIntegerRSA(43,59,11);
		rsaPQE.PrintInformation();
		// crack and build keys from a public key:
		System.out.println();
		System.out.println("Cracked key from the public key of the random 32 bit key:");
		BigIntegerRSA rsaCracked = new BigIntegerRSA(rsaRandom32.PublicKey());
		rsaCracked.PrintInformation();
		// encrypt a plain text message:
		System.out.println();
		System.out.println("Encrypted message using the random 32 bit key: 'this is a message'");
		BigInteger[] messageEncrypted = rsaRandom32.Encrypt("this is a message");
		PrintInts(messageEncrypted);
		// decrypt an array of numbers:
		System.out.println();
		System.out.println("Decrypt message using the random 32 bit key:");
		String messageDecrypted = rsaRandom32.Decrypt(messageEncrypted);
		System.out.println(messageDecrypted);
	}

	// Print a list of integers. (keys or messages)
	public static void PrintInts(BigInteger[] numbers) {
		for (int i=0; i<numbers.length; i++) {
			System.out.println(numbers[i]);
		}
	}
	public static void PrintInts(int[] numbers) {
		for (int i=0; i<numbers.length; i++) {
			System.out.println(numbers[i]);
		}
	}
}
