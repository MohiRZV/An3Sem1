package domain;

public class CerereAcc extends Cerere{
    int idFunctionar;

    public CerereAcc(int codCerere, int identificatorParking, int idFunctionar) {
        super(codCerere, identificatorParking);
        this.idFunctionar = idFunctionar;
    }

    public int getIdFunctionar() {
        return idFunctionar;
    }

    public void setIdFunctionar(int idFunctionar) {
        this.idFunctionar = idFunctionar;
    }
}
