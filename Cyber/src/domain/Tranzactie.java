package domain;

public class Tranzactie {
    int codWalletUtilizator;
    int valoareTranzactie;
    int getCodWalletDestinatar;

    public Tranzactie(int codWalletUtilizator, int valoareTranzactie, int getCodWalletDestinatar) {
        this.codWalletUtilizator = codWalletUtilizator;
        this.valoareTranzactie = valoareTranzactie;
        this.getCodWalletDestinatar = getCodWalletDestinatar;
    }

    public int getCodWalletUtilizator() {
        return codWalletUtilizator;
    }

    public void setCodWalletUtilizator(int codWalletUtilizator) {
        this.codWalletUtilizator = codWalletUtilizator;
    }

    public int getValoareTranzactie() {
        return valoareTranzactie;
    }

    public void setValoareTranzactie(int valoareTranzactie) {
        this.valoareTranzactie = valoareTranzactie;
    }

    public int getGetCodWalletDestinatar() {
        return getCodWalletDestinatar;
    }

    public void setGetCodWalletDestinatar(int getCodWalletDestinatar) {
        this.getCodWalletDestinatar = getCodWalletDestinatar;
    }

    @Override
    public String toString() {
        return "Tranzactie{" +
                "codWalletUtilizator=" + codWalletUtilizator +
                ", valoareTranzactie=" + valoareTranzactie +
                ", getCodWalletDestinatar=" + getCodWalletDestinatar +
                '}';
    }
}
