using System;

using ExamenPPD2017.Controllers;
using ExamenPPD2017.UI;

namespace ExamenPPD2017
{
    /// <summary>
    /// Main class.
    /// </summary>
    class MainClass
    {
        /// <summary>
        /// The entry point of the program, where the program control starts and ends.
        /// </summary>
        /// <param name="args">The command-line arguments.</param>
        public static void Main(string[] args)
        {
            int nrThreads;

            if (args.Length >= 1)
            {
                nrThreads = int.Parse(args[0]);
            }
            else
            {
                throw new ArgumentException("This executable must receive at least one argument (the number of threads)");
            }

            MyController controller = new MyController(nrThreads, "numere.txt");

            MainMenu mainMenu = new MainMenu(controller);
            mainMenu.Run();
        }
    }
}
