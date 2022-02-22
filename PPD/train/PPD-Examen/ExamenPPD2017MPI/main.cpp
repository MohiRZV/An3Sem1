#include "main.h"

int numbersCount = 27;
int polinomialCoefficientCount = 5;
vector<int> numbers;

// Loads the numbers from file into a vector.
vector<int> read_from_file_vector(int size) {
    vector<int> v;
    ifstream myfile(FILE_NAME);
    int k = 0;

    if (myfile.is_open())
    {
        string line;
        while (getline(myfile, line) && size > 0)
        {
            v.push_back(stoi(line));
            --size;
        }
        myfile.close();
    }
    else {
        cout << "Unable to open file " << endl;
        //throw exception("Unable to open file");
    }

    return v;
}

// the chuck that a thread should be working with.
// numbers_count is the total number of numbers
// vector_size means the number of operations (numbers) assigned to it.
chunk getStartEndById(const int& id, const int& numbers_count, const int& vector_size) {
    chunk chunk;
    int nrOfElementsPerProcess = vector_size / numbers_count;

    // doar pt. două thread-uri

    if (id == numbers_count - 1) {
        chunk.startI = id * nrOfElementsPerProcess;
        chunk.endI = vector_size;
    }
    else {
        chunk.startI = id * nrOfElementsPerProcess;
        chunk.endI = (id + 1)*nrOfElementsPerProcess;
    }

    return chunk;
}

int functionS(int position) {
    float s = 0.0f;

    // Suma elementelor din a, până la poziția dată.
    for (int i = 0; i <= position; i++)
    {
        s += numbers[i];
    }

    return s;
}

int functionF(int x) {
    float f = 0.0f;

    // Polinomul.
    for (int c = 1; c <= polinomialCoefficientCount; c++)
    {
        f += c * (float)pow(x, c);
    }

    return f;
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);
    int nrOfProcesses, processId;

    MPI_Comm_size(MPI_COMM_WORLD, &nrOfProcesses); // get nr of processes
    MPI_Comm_rank(MPI_COMM_WORLD, &processId);     // get process id

    numbers = read_from_file_vector(numbersCount);

    MPI_Finalize();
    return 0;
}

