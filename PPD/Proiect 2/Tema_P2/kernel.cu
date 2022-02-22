#include "cuda_runtime.h"
#include "device_launch_parameters.h"

#include <stdio.h>
#include <iostream>
#include <random>
#include <time.h>
#include <fstream>
#include <strstream>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/opencv.hpp>

using std::cout;
using std::endl;
using std::ifstream;
using std::ofstream;
using std::string;

#define THREADS_NO 512


cudaError_t blurImageWithCuda(unsigned char* matrix, unsigned char* result_matrix, float* filter, int rows, int columns);


__global__ void blurImageKernel(unsigned char* matrix, unsigned char* result, float* filter, int rows, int columns, int filter_rows, int filter_columns)
{
    int index = blockIdx.x * blockDim.x + threadIdx.x;
    if (index < rows * columns) {
        int row = index / columns;
        int column = index % columns;

        int limSup = row - filter_rows / 2;
        int limInf = row + filter_rows / 2;
        int limStg = column - filter_columns / 2;
        int limDr = column + filter_columns / 2;

        int linieStartFiltru = 0;
        int linieStopFiltru = filter_rows;
        int colStartFiltru = 0;
        int colStopFiltru = filter_columns;

        if (limSup < 0) {
            //depasire limita superioara
            linieStartFiltru = 0 - limSup;
            limSup = 0;
        }
        if (limInf > rows - 1) {
            //depasire limita inferioara
            linieStopFiltru = filter_rows - (limInf - rows + 1);
            limInf = rows - 1;
        }
        if (limStg < 0) {
            //depasire limita stanga
            colStartFiltru = 0 - limStg;
            limStg = 0;
        }

        if (limDr > columns - 1) {
            //depasire limita dreapta
            colStopFiltru = filter_columns - (limDr - columns + 1);
            limDr = columns - 1;
        }

        int linieFiltru = linieStartFiltru, colFiltru = colStartFiltru;
        char sum = 0;
        for (int i = limSup; i <= limInf; i++) {
            for (int j = limStg; j <= limDr; j++) {
                sum += matrix[i * columns + j] * filter[linieFiltru * filter_columns + colFiltru];
                colFiltru++;
            }
            linieFiltru++;
            colFiltru = colStartFiltru;
        }
        result[row * rows + column] = sum / (filter_columns * filter_rows);
    }

}


void filter_gaussian_init(float* filter) {
    filter[0] = 1;
    filter[1] = 1;
    filter[2] = 1;
    filter[3] = 1;
    filter[4] = 1;
    filter[5] = 1;
    filter[6] = 1;
    filter[7] = 1;
    filter[8] = 1;
}


int main()
{


    cv::Mat inputImageRGBA;
    cv::Mat outputImageRGBA;

    uchar4* inputImageRGBAMatrix;
    uchar4* outputImageRGBAMatrix;

    string input_file{ "portrait.jpg" };
    string output_file{ "portrait_blured.jpg" };
    cv::cvtColor(cv::imread(input_file.c_str(),1), inputImageRGBA, 2);

    if (inputImageRGBA.empty()) {
        std::cerr << "Couldn't open file: " << input_file << std::endl;
        exit(1);
    }

    int numRows = inputImageRGBA.rows;
    int numCols = inputImageRGBA.cols;

    inputImageRGBAMatrix = new uchar4[numRows * numCols];
    outputImageRGBAMatrix = new uchar4[numRows * numCols];
    
    outputImageRGBA.create(numRows, numCols, CV_8UC4);

    memcpy(inputImageRGBAMatrix, (uchar4*)inputImageRGBA.ptr<unsigned char>(0), numRows*numCols*sizeof(uchar4));
    
    const size_t numPixels = numRows * numCols ;

    unsigned char* red = new unsigned char[numPixels];
    unsigned char* blue = new unsigned char[numPixels];
    unsigned char* green = new unsigned char[numPixels];

    unsigned char* redBlurred = new unsigned char[numPixels];
    unsigned char* blueBlurred = new unsigned char[numPixels];
    unsigned char* greenBlurred = new unsigned char[numPixels];

    float* filter = new float[3 * 3];

    filter_gaussian_init(filter);

    
    for (size_t i = 0; i < numRows * numCols; ++i) {
        uchar4 rgba = inputImageRGBAMatrix[i];
        red[i] = rgba.x;
        green[i] = rgba.y;
        blue[i] = rgba.z;
    }


    cudaError_t cudaStatus = blurImageWithCuda(red, redBlurred, filter, numRows, numCols);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "blurWithCuda failed!");
        return 1;
    }
    cudaStatus = blurImageWithCuda(green, greenBlurred, filter, numRows, numCols);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "blurWithCuda failed!");
        return 1;
    }
    cudaStatus = blurImageWithCuda(blue, blueBlurred, filter, numRows, numCols);

    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "blurWithCuda failed!");
        return 1;
    }

    for (size_t i = 0; i < numRows * numCols; ++i) {
        uchar4 rgba = make_uchar4(redBlurred[i], greenBlurred[i], blueBlurred[i], 255);
        outputImageRGBAMatrix[i] = rgba;
    }

    memcpy((uchar4*)outputImageRGBA.ptr<unsigned char>(0), outputImageRGBAMatrix, numRows*numCols*sizeof(uchar4));

    cv::Mat imageOutputRGB;
    cv::cvtColor(outputImageRGBA, imageOutputRGB, 3);
    cv::imwrite(output_file.c_str(), imageOutputRGB);

    cudaStatus = cudaDeviceReset();
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaDeviceReset failed!");
        return 1;
    }

    delete[] red;
    delete[] green;
    delete[] blue;

    delete[] redBlurred;
    delete[] greenBlurred;
    delete[] blueBlurred;
    
    
    return 0;
}


cudaError_t blurImageWithCuda(unsigned char* matrix, unsigned char* result_matrix, float* filter, int rows, int columns)
{
    unsigned char* dev_matrix = 0;
    unsigned char* dev_result = 0;
    float* dev_filter = 0;
    cudaError_t cudaStatus;

    cudaStatus = cudaSetDevice(0);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaSetDevice failed!  Do you have a CUDA-capable GPU installed?");
        goto Error;
    }

    cudaStatus = cudaMalloc((void**)&dev_result, rows * columns * sizeof(unsigned char));
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMalloc failed!");
        goto Error;
    }

    cudaStatus = cudaMalloc((void**)&dev_matrix, rows * columns * sizeof(unsigned char));
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMalloc failed!");
        goto Error;
    }

    cudaStatus = cudaMalloc((void**)&dev_filter, 3 * 3 * sizeof(float));
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMalloc failed!");
        goto Error;
    }

    cudaStatus = cudaMemcpy(dev_matrix, matrix, rows * columns * sizeof(unsigned char), cudaMemcpyHostToDevice);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMemcpy failed!");
        goto Error;
    }

    cudaStatus = cudaMemcpy(dev_result, result_matrix, rows * columns * sizeof(unsigned char), cudaMemcpyHostToDevice);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMemcpy failed!");
        goto Error;
    }

    cudaStatus = cudaMemcpy(dev_filter, filter, 3 * 3 * sizeof(float), cudaMemcpyHostToDevice);

    int blocksNo = rows * columns / (THREADS_NO - 1);
    blurImageKernel <<<blocksNo, THREADS_NO>>> (dev_matrix, dev_result, dev_filter, rows, columns, 3, 3);

    cudaStatus = cudaGetLastError();
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "blurKernel launch failed: %s\n", cudaGetErrorString(cudaStatus));
        goto Error;
    }
    
    cudaStatus = cudaDeviceSynchronize();
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaDeviceSynchronize returned error code %d after launching addKernel!\n", cudaStatus);
        goto Error;
    }

    cudaStatus = cudaMemcpy(result_matrix, dev_result, rows * columns * sizeof(unsigned char), cudaMemcpyDeviceToHost);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMemcpy failed!");
        goto Error;
    }

Error:
    cudaFree(dev_matrix);
    cudaFree(dev_result);
    cudaFree(dev_filter);

    return cudaStatus;

}