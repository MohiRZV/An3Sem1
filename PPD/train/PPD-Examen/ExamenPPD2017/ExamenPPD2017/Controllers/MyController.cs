using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Threading;

namespace ExamenPPD2017.Controllers
{
    /// <summary>
    /// My controller.
    /// </summary>
    // TODO: Improve statics. It's a mess currently.
    public class MyController
    {
        /// <summary>
        /// Gets the thread count.
        /// </summary>
        /// <value>The thread count.</value>
        public int ThreadCount { get; private set; }

        /// <summary>
        /// Gets the name of the file.
        /// </summary>
        /// <value>The name of the file.</value>
        public string FileName { get; private set; }

        /// <summary>
        /// Gets or sets the polinomial coefficient count.
        /// </summary>
        /// <value>The polinomial coefficient count.</value>
        public static int PolinomialCoefficientCount { get; set; }

        static int numbersCount;
        static float[] arrayA;
        //static float[] arrayB;

        /// <summary>
        /// Initializes a new instance of the <see cref="T:ExamenPPD2017.Controllers.MyController"/> class.
        /// </summary>
        /// <param name="nrThreads">Nr threads.</param>
        /// <param name="fileName">File name.</param>
        public MyController(int nrThreads, string fileName)
        {
            ThreadCount = nrThreads;
            FileName = fileName;
            PolinomialCoefficientCount = 5;
        }

        /// <summary>
        /// Generates the numbers.
        /// </summary>
        public void GenerateNumbers()
        {
            Random random = new Random();

            // Golim fisierul dacă există, dacă nu există creem unul gol
            File.WriteAllText(FileName, string.Empty);
            StreamWriter file = new StreamWriter(FileName);

            //int n = random.Next(20, 100);
            int n = 100000;
            file.WriteLine(n);

            for (int i = 0; i < n; i++)
            {
                float integer = random.Next(0, 10);
                float fractionary = (float)random.NextDouble();

                //fractionary = (float)Math.Round(fractionary, 2);

                float nr = integer + fractionary;

                file.WriteLine(nr);
            }

            file.Close();
        }

        /// <summary>
        /// Loads the numbers.
        /// </summary>
        public void LoadNumbers()
        {
            StreamReader file = new StreamReader(FileName);

            numbersCount = int.Parse(file.ReadLine());

            arrayA = new float[numbersCount];
            //arrayB = new float[numbersCount];

            for (int i = 0; i < numbersCount; i++)
            {
                arrayA[i] = float.Parse(file.ReadLine());
            }

            file.Close();
        }

        /// <summary>
        /// Runs the calculations.
        /// </summary>
        public void RunCalculations()
        {
            // Load automat în caz că uit să fac manual din consolă
            if (numbersCount == 0)
            {
                LoadNumbers();
            }

            List<Thread> threads = new List<Thread>(ThreadCount);
            DateTime startTime = DateTime.Now;

            // Numărul inițial de operații per thread
            int operationsCount = numbersCount / ThreadCount;

            // Restul de operații rămase.
            int leftoverOperationsCount = numbersCount - operationsCount * ThreadCount;

            int lastI = -1;

            for (int p = 0; p < ThreadCount; p++)
            {
                int startI = lastI + 1;
                int endI = startI + operationsCount - 1;

                // Balansarea restului de operații pe thread-uri.
                // Alocăm câte o operație în plus pe fiecare thread atât timp cât avem rest de operații
                if (leftoverOperationsCount > 0)
                {
                    endI += 1;
                    leftoverOperationsCount -= 1;
                }

                lastI = endI;

                int threadNumber = p;

                Thread th = new Thread(() => RunCalculationsRange(threadNumber, startI, endI));
                threads.Add(th);
            }

            foreach (Thread th in threads)
            {
                th.Start();
                th.Join();
            }

            DateTime endTime = DateTime.Now;
            TimeSpan elpasedTime = endTime - startTime;

            // TODO: Mut print-ul din controller
            Console.WriteLine("Elpased time: " + elpasedTime);
        }

        static void RunCalculationsRange(int threadNumber, int startI, int endI)
        {
            Console.WriteLine($"[DEBUG] {threadNumber}: start={startI}, end={endI}, count={endI - startI}");

            string fileName = $"rezultat_{threadNumber}.txt";

            // Golim fisierul dacă există, dacă nu există creem unul gol
            File.WriteAllText(fileName, string.Empty);
            StreamWriter file = new StreamWriter(fileName);

            for (int i = startI; i < endI; i++)
            {
                float s = FunctionS(i);
                float f = FunctionF(s);

                //arrayB[i] = f;

                //file.WriteLine(arrayB[i]);
                file.WriteLine(f);
            }

            file.Close();
        }

        static float FunctionS(int position)
        {
            float s = 0.0f;

            // Suma elementelor din a, până la poziția dată.
            for (int i = 0; i < position; i++)
            {
                s += arrayA[i];
            }

            return s;
        }

        static float FunctionF(float x)
        {
            float f = 0.0f;

            // Polinomul.
            for (int c = 1; c <= PolinomialCoefficientCount; c++)
            {
                f += c * (float)Math.Pow(x, c);
            }

            return f;
        }
    }
}
