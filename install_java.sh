#!/bin/bash

sudo yum install -y curl wget
curl -O https://download.java.net/java/GA/jdk18/43f95e8614114aeaa8e8a5fcf20a682d/36/GPL/openjdk-18_linux-x64_bin.tar.gz
tar xvf openjdk-18_linux-x64_bin.tar.gz
sudo mv jdk-18 /opt/ 
sudo tee /etc/profile.d/jdk18.sh < exports.txt
source /etc/profile.d/jdk18.sh