#!/bin/bash 
#SBATCH --qos=part3d
#SBATCH --partition=medium
module load jdk8_32
java -jar VNS.jar
