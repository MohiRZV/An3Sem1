#pragma once
#ifndef MAIN_H
#define MAIN_H

#include <iostream>
#include <mpi.h>
#include <vector>
#include <fstream>
#include <string>
#include <math.h>

using namespace std;

typedef struct Chunk {
	int startI;
	int endI;
}chunk;

#define PROGRAM 2
#define ROOT 0

#define FILE_NAME "numere.txt"
vector<int> read_from_file_vector(int numbersCount);

#endif

