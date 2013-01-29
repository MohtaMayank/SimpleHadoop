package cmu.cs.distsystems.hw1.mp;

import cmu.cs.distsystems.hw1.MigratableProcess;

public class SimpleMigratableProcess extends MigratableProcess {

	private int numIterations;
	private int maxIterations;
	
	public SimpleMigratableProcess(String[] args) {
		super(args);
		this.maxIterations = Integer.parseInt(args[0]);
		this.numIterations = 0;
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		//Does nothing
	}

	@Override
	public boolean doNextStep() {
		
		//System.out.println(numIterations);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		numIterations++;
		
		if(numIterations < maxIterations) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		String[] arg = {"anb"};
		System.out.println((new SimpleMigratableProcess(args)).getClass().getName());
	}

	@Override
	public AFFINITY getAffinity() {
		return AFFINITY.WEAK;
	}

}
