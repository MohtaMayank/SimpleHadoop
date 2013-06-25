JFLAGS = -g -nowarn
JC = javac
CLASSPATH = -cp cmu.cs.distsystems.hw3.framework:cmu.cs.distsystems.hw3.io:cmu.cs.distsystems.hw3.mapred
.SUFFIXES: .java .class

JAVA_FILES = cmu/cs/distsystems/hw3/io/*.java cmu/cs/distsystems/hw3/framework/*.java cmu/cs/distsystems/hw3/mapred/*.java

all:
		@$(JC) $(JFLAGS) $(JAVA_FILES)
		@jar cf simplemr.jar *
		@echo -e "\n Please go through README.txt for instructions on how to run.\n"

default: all

clean:
	$(RM) cmu/cs/distsystems/hw3/*.class cmu/cs/distsystems/hw1/io/*.class cmu/cs/distsystems/hw3/framework/*.class cmu/cs/distsystems/hw3/mapred/*.class ./*.log ./simplemr.jar

master: all
	@echo -e "\n\n STARTING MASTER - IT CAN ALSO ACT AS A SLAVE AND TAKE THE FOLLOWING PROCESSES \
\n   1. NaiveBayesClassifier <train_data_path> <test_data_path> <output_file> <sleep_secs> \
  \n     Example: NaiveBayesClassifier RCV1.very_small_train.txt RCV1.very_small_test.txt RCV1.very_small_results.txt 1 \n\n \
  2. NaiveWebCrawler <entrance_url> <path_to_save_page> <append> \n     Example: NaiveWebCrawler http://www.cmu.edu testcrawler.txt false \n\n \
  3. SimpleMigratableProcess <iterations_num> \n     Example: SimpleMigratableProcess 10000 \n\n"
	@java cmu.cs.distsystems.hw1.Main --master

slave: all	
	@echo -e "\n\n Starting a slave node \n Note: Following migratable processes can be started on a slave: \
\n 1. NaiveBayesClassifier <train_data_path> <test_data_path> <output_file> <sleep_secs> \
	\n     Example: NaiveBayesClassifier RCV1.very_small_train.txt RCV1.very_small_test.txt RCV1.very_small_results.txt 1 \n\n \
	2. NaiveWebCrawler <entrance_url> <path_to_save_page> <append> \n     Example: NaiveWebCrawler http://www.cmu.edu testcrawler.txt false \n\n \
	3. SimpleMigratableProcess <iterations_num> \n     Example: SimpleMigratableProcess 10000 "
	
	@echo -e "\n\n TO START SLAVE: java cmu.cs.distsystems.hw1.Main -c <master_hostname:port> [-p|-port] <portnumber> \n \
	PLEASE START MASTER FIRST AND SUPPLY IT'S PORT NUMBER"
