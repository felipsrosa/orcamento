package br.com.mirante.orcamento.services;

import java.util.List;

import br.com.mirante.orcamento.domain.Orcamento;
import br.com.mirante.orcamento.repository.OrcamentoRepository;
import br.com.mirante.orcamento.repository.OrcamentoRepositoryJdbc;

public class ListarOrcamentosServico {

	private OrcamentoRepository repositorio = new OrcamentoRepositoryJdbc();

	public List<Orcamento> listarOrcamentos() {

		return repositorio.listar();
	}

}