package cmu.cs.distsystems.hw1;

import java.io.ObjectOutput;
import java.lang.reflect.Constructor;
import java.security.PublicKey;

/**
 * Dummy class to test git repo
 * Steps to make a git repo:
 * 1. Create a Java Project in eclipse
 * 2. Team > Share (Create a new repo in the parent dir of workspace)
 * 3. Add a .gitignore file with bin/
 * 4. Make some change
 * 5. Commit to the local repo.
 * 6. Team > remote > push ... Push to remote repo (select repo by url)
 * @author mayank
 *
 */

class Test{
    public Test(String[] args){

    }
}


public class HelloWorld {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ClassNotFoundException {

        int i = 3;
		System.out.println("One more test");
		System.out.println("here");
        System.out.println("test in IntelliJ");
        String[] ar = {"1"};
        Test t = new Test(ar);
        System.out.println(t.getClass().getName());

        //Message

    }

}
