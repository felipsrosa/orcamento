package br.com.mirante.orcamento.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.mirante.orcamento.domain.Orcamento;

public class OrcamentoRepositoryJdbc implements OrcamentoRepository {

	@Override
	public int obterMaiorId() {

		Integer maiorId = null;

		try (Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost/orcamento", "root", "fel3003");
				Statement statement = conexao.createStatement()) {

			ResultSet result = statement.executeQuery("SELECT MAX(ID) AS ID FROM ORCAMENTO");
			result.next();
			maiorId = result.getInt("ID");

		} catch (SQLException exception) {
			System.out.println("Erro ao conectar ao servidor");
			exception.printStackTrace();
		}

		return maiorId;

	}

	@Override
	public void salvar(Orcamento orcamento) {

		String insertOrcamento = "INSERT INTO ORCAMENTO (ID, DESCRICAO, MES, ANO, VALOR_TOTAL_INFORMADO) VALUES (?,?,?,?,?)";
		String insertItem = "INSERT INTO ITEM_ORCAMENTO (ORIGEM, CODIGO, DESCRICAO, UNIDADE, VALOR_UNITARIO, QUANTIDADE, VALOR_TOTAL_INFORMADO, ID_ORCAMENTO)"
				+ " VALUES (?,?,?,?,?,?,?,?)";

		try (Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost/orcamento", "root", "fel3003");
				var statementOrcamento = conexao.prepareStatement(insertOrcamento);
				var statementItem = conexao.prepareStatement(insertItem)) {

			statementOrcamento.setInt(1, orcamento.getId());
			statementOrcamento.setString(2, orcamento.getDescricao());
			statementOrcamento.setInt(3, orcamento.getMes());
			statementOrcamento.setInt(4, orcamento.getAno());
			statementOrcamento.setFloat(5, orcamento.getValorTotalInformado());

			statementOrcamento.executeUpdate();

			for (var item : orcamento.getItensOrcamento()) {

				statementItem.setString(1, item.getOrigem());
				statementItem.setString(2, item.getCodigo());
				statementItem.setString(3, item.getDescricaoItem());
				statementItem.setString(4, item.getUnidadeMedida());
				statementItem.setFloat(5, item.getValorUnitario());
				statementItem.setFloat(6, item.getQuantidade());
				statementItem.setFloat(7, item.getValorTotalInformado());
				statementItem.setInt(8, orcamento.getId());

			}

		} catch (SQLException exception) {
			System.out.println("Erro ao conectar ao servidor");
			exception.printStackTrace();
		}

	}

	@Override
	public List<Orcamento> listar() {

		List<Orcamento> orcamentos = new ArrayList<>();

		try (Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost/orcamento", "root", "fel3003");
				Statement statement = conexao.createStatement()) {

			ResultSet result = statement
					.executeQuery("SELECT ID, DESCRICAO, MES, ANO, VALOR_TOTAL_INFORMADO FROM ORCAMENTO");

			while (result.next()) {
				Integer id = result.getInt("ID");
				String descricao = result.getString("DESCRICAO");
				Integer mes = result.getInt("MES");
				Integer ano = result.getInt("ANO");
				Float valorTotalInformado = result.getFloat("VALOR_TOTAL_INFORMADO");
				Orcamento orcamento = new Orcamento(descricao, mes, ano, valorTotalInformado, new ArrayList<>());
				orcamento.setId(id);
				orcamentos.add(orcamento);

			}

		} catch (SQLException exception) {
			System.out.println("Erro ao conectar ao servidor");
			exception.printStackTrace();
		}
		return orcamentos;
	}

	@Override
	public Orcamento recuperar(int id) {
		Orcamento orcamento = null;

		try (Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost/orcamento", "root", "fel3003");
				Statement statement = conexao.createStatement()) {

			ResultSet result = statement.executeQuery(
					"SELECT ID, DESCRICAO, MES, ANO, VALOR_TOTAL_INFORMADO FROM ORCAMENTO WHERE ID =" + id);

			if (result.next()) {
				Integer idOrcamento = result.getInt("ID");
				String descricao = result.getString("DESCRICAO");
				Integer mes = result.getInt("MES");
				Integer ano = result.getInt("ANO");
				Float valorTotalInformado = result.getFloat("VALOR_TOTAL_INFORMADO");
				orcamento = new Orcamento(descricao, mes, ano, valorTotalInformado, new ArrayList<>());
				orcamento.setId(idOrcamento);
			}

		} catch (SQLException exception) {
			System.out.println("Erro ao conectar ao servidor");
			exception.printStackTrace();
		}
		return orcamento;
	}

}