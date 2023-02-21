#!/bin/bash

yum update -y

sudo yum install -y httpd
sudo systemctl start httpd
sudo systemctl enable httpd
cp index.html /var/www/html
cp frontend.js /var/www/html

sudo yum install -y curl wget
curl -O https://download.java.net/java/GA/jdk18/43f95e8614114aeaa8e8a5fcf20a682d/36/GPL/openjdk-18_linux-x64_bin.tar.gz
tar xvf openjdk-18_linux-x64_bin.tar.gz
sudo mv jdk-18 /opt/ 
