Authors:
Mayank Mohta (mmohta@)
Yuchen Tian (yuchent@)

The following explains how to setup the simple mapreduce cluster and run examples on them
Note that more details about the design, capabilities and limitations of our system is 
present in the report.pdf

To successfully run our program, you must check whether those components are found under the current directory:
1.cmu (directory contains all the source code)
2.simplemr.jar (mapreduce framework)
3.example1.jar example2.jar (example map reduce programs)
4.input, input2 (directories of all the input files, contains small sample input files)
5.output, output2 (directories of output)
6.cluster_config.txt (config file for deploying and launching the framework)
7.setup.py (script to automatically set up the framework across the cluster)
8.kill_cluster.py (close all the processes on remote cluster)

Any files other than the above aren't necessary.

IMPORTANT:Before any of steps below, please make sure your present working directory(pwd) is the directory where all those jars and config and setup files resides. 

Step 1: make clean

Step 2: make

Step 3: Setting up the map reduce cluster on GHC machines
Command:		 /usr/bin/python setup.py <config file> <path_of_directory_where_README.txt_is_present> <your_andrew_username>
Example: 		 /usr/bin/python setup.py cluster_config.txt DistributedSystem/HW3 mmohta

This will setup the cluster based on the hosts listed in the file "cluster_config.txt". After launch, it will ask you to type your andrew password for later execution. We provide a default config file for convenience.

Note:
	1.Our setup.py program involves a third-party Python SSH library called paramiko, which can be found in /usr/bin/python. This is the default Python for the Andrew machine. But on the GHC cluster, the /user/local/bin/python is used, which doesn't have paramiko pre-installed. So in order to make sure the setup can launch successfully everywhere, we use the absolute path to specify the exact Python distribution.

	2.As to the <path_of_directory_where_README.txt_is_present> argument, both absolute and relative paths are accepted. But when using the relative path, it should the relative path from your home directory to the directory where all the necessary jar and config files reside. This is important because the SSH client will login into your home directory first, then use the relative path to find the jar files to execute.

	3.We don't record your password and it will not be exposed. 

Step 4: Start Example Programs
Before starting the job, please create and input directory and an output directory (should already exist with this package)
and place your input files in input directory. 
Run the command(Suppose you are under the working directory where all the jars are):
java -cp example1.jar cmu.cs.distsystems.hw3.examples.ExampleProg1 cluster_config.txt example1.jar input/ output/

Similarly you can run the Example 2(Indegree count)
java -cp example2.jar cmu.cs.distsystems.hw3.examples.ExampleProg2 cluster_config.txt example2.jar input2/ output2/

For your convience, we have put very small test files under the input and input2 directory.

Step 5: The outputs should be present in output directory. There will be a number of files (based on the number of reducers 
thats is set by the application developer. For wordcount, currently we have set 2 reducers (see cmu/cs/distsystems/hw3/examples/ for the source code). Note the name of the output file starts with "part.".

Step 6: Kill the cluster
We also provide a script to clean up all the running processes other than the client machine. Just run the kill_cluster.py script will do the job

Command:	/usr/bin/python kill_cluster.py <config file> <username>
Example:  /usr/bin/python kill_cluster.py cluster_config.txt mmohta

The job tracker and task tracker logs are present should be present in the same directory where README.txt is present. 


