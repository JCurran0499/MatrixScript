#!/bin/bash

cd /home/ec2-user/MatrixScript
if [ ps -p $(<pid.txt) ]
then
   kill $(<pid.txt)
fi
