package br.univali.tcc2;

public class CodigoFonte {

	private String codigo;
	private Erros erros;

	public String getCodigo() {
		return this.codigo;
	}

	public final Erros getErros() {
		return this.erros;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
