__author__ = 'mimighostipad'

import paramiko
import sys
import getpass

def config_parser(filePath):
    config = dict()
    workNum = 0
    with open(filePath) as f:
        for line in f:
            line = line[:-1]
            if line == '' or line[0] == '#':
                continue
            else:
                fields = line.split(":")
                key,value = fields
                if key == "workerConfigNum":
                    workNum = int(value)
                if key[:6] == "worker":
                    key += str(workNum)
                config[key] = value
    return config

def get_ssh_client(host,username,password):
    client = paramiko.SSHClient()
    client.load_system_host_keys()
    client.set_missing_host_key_policy(
        paramiko.AutoAddPolicy())
    print "connecting to %s"% host
    client.connect(host,username=username,password=password)
    return client


config = config_parser("cluster_config.txt")

def setup(config,working_dir,username,password):

    jobtracker_host = config["jobTrackerHost"]
    jobtracker_client = get_ssh_client(jobtracker_host, username, password)
    jar_path = "simplemr.jar"
    config_path = "cluster_config.txt"
    #print jar_path,config_path
    jobtracker_client.exec_command("cd " + working_dir + ";java -cp " + jar_path + " cmu.cs.distsystems.hw3.framework.JobTracker " + config_path + " > job.log")

    numWorker = config["numWorkers"]
    numWorker = int(numWorker)

    for i in xrange(1,numWorker+1):
        worker_host = config["workerHost" + str(i)]
        worker_port = config["workerPort" + str(i)]
        print worker_host,worker_port

        worker_client = get_ssh_client(worker_host,username,password)
        worker_client.exec_command("cd " + working_dir + ";java -cp " + jar_path + " cmu.cs.distsystems.hw3.framework.TaskTracker " + worker_port + " " + config_path + " > task" + str(i) + ".log")


if __name__ == "__main__":
    config = config_parser(sys.argv[1])
    passwd = getpass.getpass(["Enter password"])
    setup(config,sys.argv[2],sys.argv[3],passwd)
