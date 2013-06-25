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

def kill_cluster(config,username,password):

    jobtracker_host = config["jobTrackerHost"]
    jobtracker_client = get_ssh_client(jobtracker_host, username, password)
    jar_path = "simplemr.jar"
    config_path = "cluster_config.txt"
    #print jar_path,config_path
    jobtracker_client.exec_command("ps aux | grep " + username + " |grep Tracker | awk '{print $2}' | xargs kill -9 ")

    numWorker = config["numWorkers"]
    numWorker = int(numWorker)

    for i in xrange(1,numWorker+1):
        worker_host = config["workerHost" + str(i)]
        worker_port = config["workerPort" + str(i)]
        print worker_host,worker_port

        worker_client = get_ssh_client(worker_host,username,password)

        worker_client.exec_command("ps aux | grep " + username + " |grep Tracker | awk '{print $2}' | xargs kill -9")

if __name__ == "__main__":
    config = config_parser(sys.argv[1])
    passwd = getpass.getpass(["Enter password"])
    kill_cluster(config,sys.argv[2],passwd)
