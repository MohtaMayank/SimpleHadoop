package cmu.cs.distsystems.hw1.mp;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cmu.cs.distsystems.hw1.MigratableProcess;
import cmu.cs.distsystems.hw1.TransactionalFileInputStream;

public class NaiveBayesClassifier extends MigratableProcess {

	private static final long serialVersionUID = -9056024677846252900L;
	
	//Steps in this process
	private static final String TRAIN_STEP = "Tr";
	private static final String TEST_STEP = "Test";
	private static final String PRINT_RESULT_STEP = "Res";
	
	//Constants related to classification
	public static final String AND_STRING = "^";
	public static final String MATCH_ANY_STR = "*";
	public static final String[] ALL_CLASSES = {"ECAT", "GCAT", "MCAT", "CCAT"};
	
	
	private Map<String, Integer> counts;
	private long vocabSize;
	private List<NBTestResult> testResults;
	private int numCorrect;
	private TransactionalFileInputStream trainFile;
	private TransactionalFileInputStream testFile;
	private String outputFile; 
	
	private String currentStep;
	
	private int waitSeconds = 0;
	
	public NaiveBayesClassifier(String[] args) {
		super(args);
		
		this.trainFile = new TransactionalFileInputStream(args[0]);
		this.testFile = new TransactionalFileInputStream(args[1]);
		this.vocabSize = 0;
		this.numCorrect = 0;
		this.counts = new HashMap<String, Integer>();
		this.testResults = new ArrayList<NBTestResult>();
		this.outputFile = args[2];
		this.currentStep = TRAIN_STEP;
		
		if(args.length == 4) {
			this.waitSeconds = Integer.parseInt(args[3]);
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean doNextStep() throws Exception{
		
		boolean notFinished = true;
		
		if(currentStep.equals(TRAIN_STEP)) {
			DataInputStream dis = new DataInputStream(trainFile);
			String doc = dis.readLine();
			if(doc != null) {
				train(doc);
			} else {
				//reached end of training stage - start testing
				this.currentStep = TEST_STEP;
			}
			
			notFinished = true;
		} else if (currentStep.equals(TEST_STEP)) {
			DataInputStream dis = new DataInputStream(testFile);
			String doc = dis.readLine();
			if(doc != null) {
				test(doc);
			} else {
				//reached end of training stage - start testing
				this.currentStep = PRINT_RESULT_STEP;
				
			}

			notFinished = true;
		} else if (currentStep.equals(PRINT_RESULT_STEP)) {
			printResult();
			currentStep = "END";
			notFinished = true;
		} else {
			return false;
		}
		
		
		if(waitSeconds > 0) {
			Thread.sleep(waitSeconds*1000);
		}
		
		return notFinished;
	}
	
	public void train(String currDoc) {
		Vector<String> tokens = tokenizeDoc(currDoc);
		int totalWords = tokens.size();
		Vector<String> classes = extractCATClasses(currDoc);
		
		//(Y=y) for each label y the number of training instances of that class
		for(String cl : classes) {
			Integer tmp;
			if((tmp = counts.get(cl)) != null) {
				counts.put(cl, tmp + 1);
			} else {
				counts.put(cl, 1);
			}
			
			//(Y=y,W=*) total number of tokens for documents with label y.
			if((tmp = counts.get(cl + AND_STRING + MATCH_ANY_STR)) != null) {
				counts.put(cl + AND_STRING + MATCH_ANY_STR, tmp + totalWords);
			} else {
				counts.put(cl + AND_STRING + MATCH_ANY_STR, totalWords);
			}
		}
		
		//(Y=*) here * means anything, so this is just the total number of training instances
		Integer tmp;
		if((tmp = counts.get(MATCH_ANY_STR)) != null) {
			counts.put(MATCH_ANY_STR, tmp + classes.size());
		} else {
			counts.put(MATCH_ANY_STR, classes.size());
		}
		
		for(String tok : tokens) {
			for(String cl : classes) {
				incrementVocab(tok);
				//(Y=y,W=w) number of times token w appears in a document with label y
				if((tmp = counts.get(cl + AND_STRING + tok)) != null) {
					counts.put(cl + AND_STRING + tok, tmp + 1);
				} else {
					counts.put(cl + AND_STRING + tok, 1);
				}
			}
		}
	}
	
	public void test(String currDoc) {
		Vector<String> tokens = tokenizeDoc(currDoc);
		int totalWords = tokens.size();
		Vector<String> classes = extractCATClasses(currDoc);
		
		double maxSumOfLnWords = Integer.MIN_VALUE;
		String highestProbClass = null;
		
		for(String cl : ALL_CLASSES) {
			double sumOfLnWords = 0;
			for(String tok : tokens) {
				double numer;
				if(counts.get(cl + AND_STRING + tok) != null) {
					numer = counts.get(cl + AND_STRING + tok);
				} else {
					numer = 1;
				}
				//TODO: this should be added to vocab size.
				double denom = counts.get(cl + AND_STRING + MATCH_ANY_STR) 
						+ vocabSize;
			
				sumOfLnWords += Math.log(numer) - Math.log(denom);
			}
			
			sumOfLnWords += Math.log(counts.get(cl)) - Math.log(counts.get(MATCH_ANY_STR));

			//System.out.println(cl + "\t" + sumOfLnWords);
			
			if(sumOfLnWords > maxSumOfLnWords) {
				maxSumOfLnWords = sumOfLnWords;
				highestProbClass = cl;
			}
		}
		
		testResults.add(new NBTestResult(highestProbClass, classes.toString()));
		
		if(classes.contains(highestProbClass)) {
			numCorrect++;
		}
			
	}
	
	//Currently this step is not being distributed.
	public void printResult() throws Exception {
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(new File(outputFile)));
		for(NBTestResult n : testResults ) {
			bw.write(n.getTrueClass() + "\t" + n.getClassifiedAs() + "\n");
		}
		bw.write("Percent correct:" + numCorrect + 
				"/" + testResults.size() + "=" + numCorrect/(double)testResults.size());
		
		bw.flush();
		bw.close();
	}
	
	@Override
	public AFFINITY getAffinity() {
		//Strong affinity because lot of data will have to be tranferred.
		return AFFINITY.STRONG;
	}
	
	
	public Vector<String> extractCATClasses(String currDoc) {
		String classText = currDoc.split("\\t")[0];
		String[] allClasses = classText.split(",");
		
		Vector<String> classesWithCAT = new Vector<String>();
		
		for(String s : allClasses) {
			for (String cl : ALL_CLASSES) {
				if( cl.equals(s)) {
					classesWithCAT.add(s);
				}
			}
		}
		
		return classesWithCAT;
	}

	public Vector<String> tokenizeDoc(String currDoc) {
		String docText = currDoc.split("\\t")[1];
		
		String[] words = docText.split("\\s+");
		Vector<String> tokens = new Vector<String>();
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].replaceAll("\\W", "");
			if (words[i].length() > 0) {
				tokens.add(words[i]);
			}
		}
		return tokens;
	}
	
	public String[] extractALLClasses(String currDoc) {
		String classText = currDoc.split("\\t")[0];
		return classText.split(",");
	}
	
	public void incrementVocab(String tok) {
		boolean isNew = true;
		
		//If it is not in any of the classes, then it is the first occurrence.
		for(String cl : ALL_CLASSES) {
			if(counts.get(cl + AND_STRING + tok) != null) {
				isNew = false;
			}
		}
		
		if(isNew) {
			this.vocabSize++;
		}
	}

}

class NBTestResult implements Serializable {

	private static final long serialVersionUID = -7590056285384254072L;
	
	private String classifiedAs;
	private String trueClass;
	
	public NBTestResult(String classifiedAs, String trueClass) {
		this.classifiedAs = classifiedAs;
		this.trueClass = trueClass;
	}

	public String getClassifiedAs() {
		return classifiedAs;
	}

	public String getTrueClass() {
		return trueClass;
	}
}