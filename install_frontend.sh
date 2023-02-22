#!/bin/bash

yum update -y

curl -sL https://rpm.nodesource.com/setup_14.x | sudo bash -
sudo yum install -y nodejs

cd frontend
npm install
cd ..
