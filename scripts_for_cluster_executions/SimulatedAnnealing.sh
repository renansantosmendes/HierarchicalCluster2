#!/bin/bash 
#SBATCH --qos=part3d
#SBATCH --partition=small
module load jdk8_32
java -jar SimulatedAnnealing.jar