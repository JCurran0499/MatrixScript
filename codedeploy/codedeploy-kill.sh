#!/bin/bash

cd /home/ec2-user/MatrixScript
if [ ps -p $(<codedeploy/pid.txt) ]
then
   kill -9 $(<codedeploy/pid.txt)
fi
