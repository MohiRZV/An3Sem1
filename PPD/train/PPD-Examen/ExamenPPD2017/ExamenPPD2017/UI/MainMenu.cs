using ExamenPPD2017.Controllers;

namespace ExamenPPD2017.UI
{
    /// <summary>
    /// Main menu.
    /// </summary>
    public class MainMenu : Menu
    {
        //MyController controller;

        /// <summary>
        /// Initializes a new instance of the <see cref="Lab5PPD.UI.MainMenu"/> class.
        /// </summary>
        public MainMenu(MyController controller)
        {
            Title = "Examen PPD";
            //this.controller = controller;

            AddCommand("generate", "Generates new numbers in " + controller.FileName, delegate
                {
                    controller.GenerateNumbers();
                });

            AddCommand("load", "Loads the numbers from " + controller.FileName, delegate
                {
                    controller.LoadNumbers();
                });

            AddCommand("run", "Runs the threads", delegate
                {
                    controller.RunCalculations();
                });

            AddCommand("setCoef", $"Sets the polinomial coefficient count (Default: {MyController.PolinomialCoefficientCount})", delegate
                {
                    SetCoefficientCount();
                });
        }

        void SetCoefficientCount()
        {
            int val = int.Parse(Input("Polinomial coefficient count = "));

            MyController.PolinomialCoefficientCount = val;
        }
    }
}
