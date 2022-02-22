package utils;

import com.mohi.Main;
import domain.TranzactieAcceptata;

public class Generator {
    public static void initWallets() {
        for(int i = 1; i<= Main.N; i++) {
            FileIO.writeTranzactii(new TranzactieAcceptata(-1, 100, i, -1));
        }
    }
}
