package domain;

public class TranzactieAcceptata extends Tranzactie{
    int idSupervizor;

    public TranzactieAcceptata(int codWalletUtilizator, int valoareTranzactie, int getCodWalletDestinatar, int idSupervizor) {
        super(codWalletUtilizator, valoareTranzactie, getCodWalletDestinatar);
        this.idSupervizor = idSupervizor;
    }

    public int getIdSupervizor() {
        return idSupervizor;
    }

    public void setIdSupervizor(int idSupervizor) {
        this.idSupervizor = idSupervizor;
    }

    @Override
    public String toString() {
        return "TranzactieAcceptata{" +
                "codWalletUtilizator=" + codWalletUtilizator +
                ", valoareTranzactie=" + valoareTranzactie +
                ", getCodWalletDestinatar=" + getCodWalletDestinatar +
                ", idSupervizor=" + idSupervizor +
                '}';
    }
}
