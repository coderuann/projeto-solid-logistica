package logistica.service;

import java.math.BigDecimal;

import logistica.domain.CalculadoraFrete;

import logistica.domain.Encomenda;

import logistica.domain.RepositorioFrete;

import logistica.domain.Rota;


public class ServicoCotacaoFrete {

    
    private CalculadoraFrete calculadora;
    
    private RepositorioFrete repositorio;

    
    public ServicoCotacaoFrete(CalculadoraFrete calculadora, RepositorioFrete repositorio) {
        
        this.calculadora = calculadora;
        this.repositorio = repositorio;
    }

    
    public BigDecimal cotar(Encomenda encomenda, Rota rota) {

        BigDecimal valorFrete = calculadora.calcular(encomenda, rota);
    
        repositorio.salvar(encomenda);
      
        return valorFrete;
    }
}
