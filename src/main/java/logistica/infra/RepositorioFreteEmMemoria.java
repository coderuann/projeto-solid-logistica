package logistica.infra;
import java.util.ArrayList;
import logistica.domain.Encomenda;
import logistica.domain.RepositorioFrete;

public class RepositorioFreteEmMemoria implements RepositorioFrete {

    private ArrayList<Encomenda> encomendas;

    public RepositorioFreteEmMemoria() {
        this.encomendas = new ArrayList<>();
    }

    @Override
    public void salvar(Encomenda encomenda) {
        Encomenda encomendaJaSalva = buscarPorId(encomenda.getId());
        if (encomendaJaSalva == null) {
            encomendas.add(encomenda);
        }
    }

    @Override
    public Encomenda buscarPorId(int id) {
        for (Encomenda encomenda : encomendas) {
            if (encomenda.getId() == id) {
                return encomenda;
            }
        }
        return null;
    }
}
