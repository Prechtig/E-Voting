import java.math.BigInteger;
//import java.util.Random;
import java.security.SecureRandom;
import java.util.Scanner;

// for reading/writing keys from/to a file & reading a message
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.RandomAccessFile;
import java.io.IOException;

public class ElGamalTest {
    	final int keyLength = 64;
        SecureRandom  rng = new SecureRandom();
        BigInteger p;  // triplet (p, e1, e2) is the public key
        BigInteger e1; // triplet (p, e1, e2) is the public key
        BigInteger e2; // triplet (p, e1, e2) is the public key
        BigInteger d;  // private key
        BigInteger c1; // pair (c1, c2) is the ciphertext
        BigInteger c2; // pair (c1, c2) is the ciphertext
        //BigInteger cm1[]; // pair (cm1[i], cm2[i]) is the ciphertext of i-th part of the message
        //BigInteger cm2[]; // pair (cm1[i], cm2[i]) is the ciphertext of i-th part of the message
        BigInteger Plain;
    	int MsgLength = 0;
		// File names:
        final static String publicKeyFileName    = "publicKey.txt"; // Public key
        final static String privateKeyFileName   = "privateKey.txt"; // Private key
        final static String textFileName         = "message.txt"; // File to read message from
    	final static String cipheredTextFileName = "message_encrypted.txt"; // File with ciphered message
    	final static String plainTextFileName    = "message_decrypted.txt"; // File with deciphered message

       

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try {
            System.out.println("ElGamal::main");
            Scanner s = new Scanner(System.in); // read from command line
            ElGamalTest m = new ElGamalTest();
            
            System.out.println("Do you want to generate new keys (y/n)? ");
            String generateKeys = s.next();
            if (generateKeys.equals("y") || generateKeys.equals("yes")) {
                m.generate_keys();
            } else {
                m.load_keys();
            }
            
            //System.out.println("Please, enter a number (a message to be ciphered): ");
            //BigInteger M = s.nextBigInteger();
            //m.encryption(M);
            //BigInteger Plaintext = m.decryption();
            //System.out.println(" \n Plaintext is "+Plaintext+"\n");
            byte[] Message = readBytes(textFileName);
            String MessageString = new String(Message);
            System.out.println(" \n Message is "+MessageString+"\n");
            m.encryptMessage(Message);
            byte[] PlainMessage = m.decryptMessage();

        	String Plaintext = new String(PlainMessage);
            System.out.println(" \n Plaintext is "+Plaintext+"\n");
        
        } catch (IOException exception) {
            exception.printStackTrace();
        }       
    }
    
    public void generate_keys() throws IOException
    {
        System.out.println("Generating new keys, please wait...");
        p = BigInteger.probablePrime(keyLength, rng);
        e1 = BigInteger.probablePrime(keyLength-1, rng);
        while (true)
        {
            d = BigInteger.probablePrime(keyLength-1, rng);
            if ( d.gcd(p).equals(BigInteger.ONE) )
            	break;
            else 
                continue;
        }
        e2 = e1.modPow(d, p);
        System.out.println("New keys were generated successfully!");
        
        BigInteger[] publicKey = {
            p, e1, e2
        };
        writeBigIntegerArray(publicKeyFileName, publicKey); // Save public key to file
        BigInteger[] privateKey = {
            d
        };
        writeBigIntegerArray(privateKeyFileName, privateKey); // Save private key to file
        System.out.println("New keys were saved");
    }
    
    public void load_keys() throws IOException
    {
        System.out.println("Loading keys, please wait...");
        /** Read keys **/
        // Public key order is: p, e1, e2
        String[] publicKey = readStrings(publicKeyFileName);
        // Private key order is: d
        String[] privateKey = readStrings(privateKeyFileName);

        /** Set keys to loaded values **/
        // Public key
        p = new BigInteger(publicKey[0]);
        e1 = new BigInteger(publicKey[1]);
        e2 = new BigInteger(publicKey[2]);
        // Private key
        d = new BigInteger(privateKey[0]);
        System.out.println("Keys were loaded successfully!");
    }
   
    public void encryptMessage(byte[] Msg) throws IOException
    {
        //BigInteger cm1[]; // pair (cm1[i], cm2[i]) is the ciphertext of i-th part of the message
        //BigInteger cm2[]; // pair (cm1[i], cm2[i]) is the ciphertext of i-th part of the message

        int BlockLength = 4;
        MsgLength = Msg.length;
        int k = 0;
        int n = MsgLength / BlockLength;
        int m = 0;
        if (MsgLength % BlockLength != 0)
            m = 1;
        BigInteger cm1[] = new BigInteger[n + m]; // pair (cm1[i], cm2[i]) is the ciphertext of i-th part of the message
        BigInteger cm2[] = new BigInteger[n + m]; // pair (cm1[i], cm2[i]) is the ciphertext of i-th part of the message
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(cipheredTextFileName));
        encryption(BigInteger.valueOf(MsgLength));
        writer.write(c1.toString()); writer.write(' ');
        writer.write(c2.toString()); writer.write(' ');
        for (int i = 0; i < n; i++) {
            byte Block[] = new byte[BlockLength];
            for (int j = 0; j < BlockLength; j++) {
                Block[j] = Msg[k++];//Msg[i*BlockLength + j];
            }
            BigInteger M = new BigInteger(Block);
            encryption(M); // encrypt i-th block
            cm1[i] = c1;
            cm2[i] = c2;
            writer.write(c1.toString()); writer.write(' ');
            writer.write(c2.toString()); writer.write(' ');
        }//*/
        while (k < MsgLength) {
            int l = MsgLength % BlockLength;
            byte Block[] = new byte[l];
            for (int j = 0; j < l; j++) {
                Block[j] = Msg[k++];
            }
            BigInteger M = new BigInteger(Block);
            encryption(M); // encrypt last block
            cm1[n] = c1;
            cm2[n] = c2;
            writer.write(c1.toString()); writer.write(' ');
            writer.write(c2.toString()); writer.write(' ');
        }
        writer.close();
    }
    
    /* decrypt a message by decrypting separate blocks */
    public byte[] decryptMessage() throws IOException
    {
        /** Read encrypted message **/
        String[] cipheredText = readStrings(cipheredTextFileName);
        BigInteger tmp = new BigInteger(cipheredText[0]);
        MsgLength = (int) tmp.longValue();
        byte[] Msg = new byte[MsgLength];
        int k = 0;
        for (int i = 0; i < (cipheredText.length-1)/2; i++)
        {
            //c1 = cm1[i];
            //c2 = cm2[i];
            c1 = new BigInteger(cipheredText[i*2+1]);
            c2 = new BigInteger(cipheredText[i*2+2]);
            BigInteger P = decryption();
            byte[] Block = P.toByteArray();
            for (int j = 0; j < Block.length; j++) {
                Msg[k++] = Block[j];
            }
        }
        return Msg;
    }
    
    public void encryption(BigInteger M)
    {
        BigInteger r  = BigInteger.probablePrime(keyLength, rng); // generate new session key

        /////M.multiply(e2.modPow(r, p)).mod(p);
        c1 = e1.modPow(r, p); // first part of ciphertext
        //c2 = M.multiply(e2.modPow(r, p)).mod(p); // second part of ciphertext
        c2 = ((M.mod(p)).multiply(e2.modPow(r, p))).mod(p); // second part of ciphertext
        // (c1, c2) is the ciphertext
		System.out.println("r = " + r);
        System.out.print("Ciphertext c1 is "+c1+"\nCiphertext c2 is "+c2 + "\n");
    }
    
    public BigInteger decryption()
    {
        BigInteger P ;
        
        P = ((c1.modPow(d, p)).modInverse(p)).multiply(c2).mod(p);
        //P = ((c2.mod(p)).multiply(c1.modPow(d.modInverse(p), p))).mod(p);
        return P;
    }
    
    /**
     * Writes an array of big integers as strings, separated by ' '
     */    
    public static final void writeBigIntegerArray(String fileName, BigInteger[] bigNumbers) throws IOException {
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(fileName));
        for (BigInteger number : bigNumbers) {
            writer.write(number.toString());
            writer.write(' ');
        }
        writer.close();
    }
    /**
     * Reads strings, separated by ' '
     */
    public static final String[] readStrings(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new java.io.FileReader(fileName));
        String fileContent = reader.readLine();
        String[] result = fileContent.split("\\s");
        return result;
    }
    /**
     * Reads bytes from file
     *
     * @param fileName file name to read bytes from
     * @return all bytes
     */
    public static final byte[] readBytes(String fileName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(fileName, "r");
        byte[] fileContent = new byte[(int) file.length()];
        file.read(fileContent);
        return fileContent;
    }
}