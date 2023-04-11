#!/bin/bash

cd /home/ec2-user/MatrixScript
kill -9 $(<pid.txt)
