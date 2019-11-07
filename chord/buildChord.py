"""
run the cluster!
"""


import subprocess
import os
import sys
import time


clientTag = "chord-20:client"
anchorTag = "chord-20:anchor"
clientDockerFile = "Dockerfile.client"
anchorDockerFile = "Dockerfile.anchor"
networkName = "chord-20-net"
ipRange = "172.10.0.0/16"
anchorIP = "172.10.0.2"
anchorPort = 30000
clientPort = 40000

def buildChord():
    buildDocker()
    createNetwork(networkName)


def buildDocker():
    #check for tag name "cscsi251-Netti-worker"
    if not dockerImageExists(clientTag):
        print("build client image")
        buildDockerImage(clientDockerFile, clientTag)
    if not dockerImageExists(anchorTag):
        print("build anchor image")
        buildDockerImage(anchorDockerFile, anchorTag)


def createNetwork(name):
    if not networkExists(name):
        print("build network");
        os.system("docker network create --subnet=" + ipRange + " " + name)


def networkExists(name):
    out = subprocess.check_output(["docker", "network", "ls", "-q", "-f", "name="+name])
    if out != b'':
        return True
    else:
        return False


def dockerImageExists(tag):
    """
    checks if an image with a certain tag exists

    @param tag: tag of image
    @return: true or false if image exists
    """
    
    out = subprocess.check_output(["docker", "images", "-q", tag])
    if out != b'':
        return True
    else:
        return False


def buildDockerImage(location, tag):
    out = subprocess.check_output(["docker", "build", "--rm", "-f", location, "--tag="+tag, "."])


buildChord()
# use -rm to remove container after stopped
