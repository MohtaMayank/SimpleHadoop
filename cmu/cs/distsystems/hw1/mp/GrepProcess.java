package cmu.cs.distsystems.hw1.mp;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;

import cmu.cs.distsystems.hw1.MigratableProcess;
import cmu.cs.distsystems.hw1.TransactionalFileInputStream;
import cmu.cs.distsystems.hw1.TransactionalFileOutputStream;
import cmu.cs.distsystems.hw1.MigratableProcess.AFFINITY;

public class GrepProcess extends MigratableProcess {

	private static final long serialVersionUID = 7162754277807079533L;
	
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;

	public GrepProcess(String args[]) throws Exception
	{
		super(args);
		if (args.length != 3) {
			System.out.println("usage: GrepProcess <queryString> <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}

		inFile = new TransactionalFileInputStream(args[1]);
		outFile = new TransactionalFileOutputStream(args[2], false);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub 
	}

	@Override
	public boolean doNextStep() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AFFINITY getAffinity() {
		// TODO Auto-generated method stub
		return AFFINITY.MEDIUM;
	}

}
