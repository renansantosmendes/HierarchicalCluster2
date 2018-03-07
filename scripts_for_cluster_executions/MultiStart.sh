#!/bin/bash 
#SBATCH --qos=part1d
#SBATCH --partition=medium
module load jdk8_32
java -jar MultiStart.jar