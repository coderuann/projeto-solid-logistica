
package logistica.service;

import logistica.domain.Encomenda;

import logistica.domain.Motorista;

import logistica.domain.NotificadorEntrega;

import logistica.domain.RepositorioFrete;

import logistica.domain.Veiculo;


public class ServicoDespachoFrete {

    private NotificadorEntrega notificador;
    
    private RepositorioFrete repositorio;

   
    public ServicoDespachoFrete(NotificadorEntrega notificador, RepositorioFrete repositorio) {
        
        this.notificador = notificador;
       
        this.repositorio = repositorio;
    }

    
    public void despachar(int idEncomenda, Motorista motorista) {
        
        Encomenda encomenda = repositorio.buscarPorId(idEncomenda);

        
        if (encomenda == null) {
           
            System.out.println("Encomenda " + idEncomenda + " não encontrada. Despacho cancelado.");
         
            return;
        }

        
        Veiculo veiculo = motorista.getVeiculo();

        boolean cabeNoVeiculo = veiculo.suportaPeso(encomenda.getPeso());

        if (cabeNoVeiculo == false) {
          
            notificador.notificar(encomenda, "AGUARDANDO VEÍCULO MAIOR");
            
            return;
        }

       
        System.out.println("Encomenda " + encomenda.getId() + " entregue ao motorista "
                + motorista.getNome() + " (" + veiculo.getTipo() + " placa " + veiculo.getPlaca() + ").");
        
        notificador.notificar(encomenda, "SAIU PARA ENTREGA");
    }
}
