#!/bin/bash

SOURCEFILE="$1"
mpic++ -O3 $SOURCEFILE
mpirun -np 2 ./a.out
