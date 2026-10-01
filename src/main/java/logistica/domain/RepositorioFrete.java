package logistica.domain;

public interface RepositorioFrete {

    void salvar(Encomenda encomenda);

    Encomenda buscarPorId(int id);
}
