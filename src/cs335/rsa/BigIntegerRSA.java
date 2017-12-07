package cs335.rsa;

import java.security.SecureRandom;
import java.math.*;

public class BigIntegerRSA {
	private int BITLEN;		   // RSA key length (bits)
	private BigInteger P;	   // RSA prime P
	private BigInteger Q;      // RSA prime Q
	private BigInteger N;      // N = PQ
	private BigInteger PhiN;   // PhiN = (P-1)(Q-1) = encryption modulus
	private BigInteger E;      // encryption exponent
	private BigInteger D;	   // decryption exponent
	
	// Generates random keys of BITLEN length. 
	public BigIntegerRSA(int BITLEN) {
		this.BITLEN = BITLEN;
		// generate P and Q
		SecureRandom r = new SecureRandom();
		P = BigInteger.ZERO;
		Q = BigInteger.ZERO;
		while (P.equals(Q)) {
			P = new BigInteger(BITLEN / 2, 100, r);
			Q = new BigInteger(BITLEN / 2, 100, r);
		}
		calculateNED();
	}

	// Build keys from specific primes P and Q.
	public BigIntegerRSA(BigInteger P, BigInteger Q) {
		this.P = P;
		this.Q = Q;
		if (P.intValue() > 2 && Q.intValue() > 2 && ! P.equals(Q) && 
			P.isProbablePrime(64) && Q.isProbablePrime(64)) {
			calculateNED();
		} else {
			System.out.println("P=" + P + " and Q=" + Q + " are invalid primes.");
		}
	}
	public BigIntegerRSA(int p, int q) {
		this(BigInteger.valueOf(p), BigInteger.valueOf(q));
	}
	
	// Build keys from P, Q, and E.
	public BigIntegerRSA(BigInteger P, BigInteger Q, BigInteger E) {
		this.P = P;
		this.Q = Q;
		this.E = E;
		if (P.intValue() > 2 && Q.intValue() > 2 && ! P.equals(Q) && 
			P.isProbablePrime(64) && Q.isProbablePrime(64)) {
			calculateNED();
		} else {
			System.out.println("P=" + P + " and Q=" + Q + " are invalid primes.");
		}
	}
	public BigIntegerRSA(int p, int q, int e) {
		this(BigInteger.valueOf(p), BigInteger.valueOf(q), BigInteger.valueOf(e));
	}
	
	// Crack and build keys from public key N and E.
	public BigIntegerRSA(BigInteger[] publicKey) {
		N = publicKey[0];
		E = publicKey[1];
		P = new BigInteger("3");
		BigInteger TWO = new BigInteger("2");
		// factor N:
		while ( ! N.remainder(P).equals(BigInteger.ZERO) ) {
			P = P.add(TWO);
		}
		Q = N.divide(P);
		calculateNED();
	}

	// Calculate N, PhiN, E, and D.
	private void calculateNED() {
		// use smaller prime as P (arbitrary for RSA, but prints nicer!)
		if (P.compareTo(Q) == 1) {
			BigInteger temp = P;
			P = Q;
			Q = temp;
		}
		N = P.multiply(Q);
		PhiN = (P.subtract(BigInteger.ONE)).multiply(Q.subtract(BigInteger.ONE));
		// pick the lowest valid E if none is given
		if (E == null) {
			E = new BigInteger("2");
		}
		while (PhiN.gcd(E).compareTo(BigInteger.ONE) > 0) {
			E = E.add(BigInteger.ONE);
		}		
		// calculate D
		D = E.modInverse(PhiN);	
	}
	
	// Get public key.
	public BigInteger[] PublicKey() {
		BigInteger[] publicKey = new BigInteger[2];
		publicKey[0] = N;
		publicKey[1] = E;
		return publicKey;
	}
	
	// Get private key.
	public BigInteger[] PrivateKey() {
		BigInteger[] privateKey = new BigInteger[2];
		privateKey[0] = N;
		privateKey[1] = D;
		return privateKey;
	}
	
	// Get primes.
	public BigInteger[] Primes() {
		BigInteger[] primes = new BigInteger[2];
		primes[0] = P;
		primes[1] = Q;
		return primes;
	}
	
	// Print RSA key values.
	public BigInteger[] PrintInformation() {
		BigInteger[] info = new BigInteger[5];
		System.out.println("P: " + P);
		System.out.println("Q: " + Q);
		System.out.println("N: " + N);
		System.out.println("E: " + E);
		System.out.println("D: " + D);
		info[0] = P;
		info[1] = Q;
		info[2] = N;
		info[3] = E;
		info[4] = D;
		return info;
	}
	
	// Encrypt a plain text message as a list of BigIntegers.
	public BigInteger[] Encrypt(String textMessage) {
		BigInteger[] encryptedMessage = new BigInteger[textMessage.length()];
		BigInteger C;
		BigInteger M;
		for (int i=0; i<textMessage.length(); i++) {
			M = new BigInteger(((int)textMessage.charAt(i)) + "");
			C = M.modPow(E, N);
			encryptedMessage[i] = C;
		}
		return encryptedMessage;
	}
	
	// Decrypt an array of integers back to a string.
	public String Decrypt(BigInteger[] encryptedMessage) {
		String textMessage = "";
		BigInteger C;
		BigInteger M;
		for (int i=0; i<encryptedMessage.length; i++) {
			C = encryptedMessage[i];
			M = C.modPow(D, N);
			textMessage += (char)(M.intValue());
		}	
		return textMessage;
	}
	public String Decrypt(int[] encryptedMessage) {
		BigInteger[] encryptedMessageBig = new BigInteger[encryptedMessage.length];
		for (int i=0; i<encryptedMessage.length; i++) {
			encryptedMessageBig[i] = BigInteger.valueOf(encryptedMessage[0]);
		}
		return Decrypt(encryptedMessageBig);
	}
}

