package br.com.arius.levantamento.model;

public class QrTrocaVO {

	private long produto;
	private String descritivo_produto;
	private String estoque_atual;

	public long getProduto() {
		return produto;
	}

	public void setProduto(long produto) {
		this.produto = produto;
	}

	public String getDescritivo_produto() {
		return descritivo_produto;
	}

	public void setDescritivo_produto(String descritivo_produto) {
		this.descritivo_produto = descritivo_produto;
	}

	public String getEstoque_atual() {
		return estoque_atual;
	}

	public void setEstoque_atual(String estoque_atual) {
		this.estoque_atual = estoque_atual;
	}

}
