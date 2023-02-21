#!/bin/bash

yum update -y

sudo yum install -y httpd
sudo systemctl start httpd
sudo systemctl enable httpd
cp frontend/index.html /var/www/html
cp frontend/frontend.js /var/www/html

curl -sL https://rpm.nodesource.com/setup_14.x | sudo bash -
sudo yum install -y nodejs

cd frontend
npm install
cd ..
