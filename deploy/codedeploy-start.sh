#!/bin/bash

cd /home/ec2-user/MatrixScript-Backend || exit
java -jar target/MatrixScript-1.0-jar-with-dependencies.jar > /dev/null 2> /dev/null < /dev/null &
echo $! > deploy/pid.txt
