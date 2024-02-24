#!/bin/bash

cd /home/ec2-user/MatrixScript-Backend || exit
if ps -p "$(<deploy/pid.txt)"
then
   sudo kill -9 "$(<deploy/pid.txt)"
fi
