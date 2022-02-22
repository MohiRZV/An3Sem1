package domain;

public class Cerere {
    int codCerere;
    int identificatorParking;

    public Cerere(int codCerere, int identificatorParking) {
        this.codCerere = codCerere;
        this.identificatorParking = identificatorParking;
    }

    public int getCodCerere() {
        return codCerere;
    }

    public void setCodCerere(int codCerere) {
        this.codCerere = codCerere;
    }

    public int getIdentificatorParking() {
        return identificatorParking;
    }

    public void setIdentificatorParking(int identificatorParking) {
        this.identificatorParking = identificatorParking;
    }
}
