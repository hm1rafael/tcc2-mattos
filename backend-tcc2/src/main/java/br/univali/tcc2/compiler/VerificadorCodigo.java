package br.univali.tcc2.compiler;

import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.compilers.JavaCompilerFactory;
import org.apache.commons.jci.problems.CompilationProblem;
import org.apache.commons.jci.readers.MemoryResourceReader;
import org.apache.commons.jci.stores.FileResourceStore;
import org.apache.commons.jci.stores.ResourceStore;
import org.springframework.stereotype.Component;

import br.univali.tcc2.DesafioUsuario;
import br.univali.tcc2.Erro;
import br.univali.tcc2.Erros;
import br.univali.tcc2.PosicaoErro;
import br.univali.tcc2.exception.ErroCompilacaoException;

/**
 * Classe com o papel de verificar os dados do desafio do usuario, compilar o codigo e caso haja algum erro, agrupar os erros para demonstrar na tela (seja web ou qualquer outro provider)
 * @author rafael
 *
 */
@Component
public class VerificadorCodigo {

	private static final String ECLIPSE = "eclipse";

	public void compilarCodigo(DesafioUsuario desafioUsuario) {
		JavaCompiler javaCompiler = new JavaCompilerFactory().createCompiler(ECLIPSE);
		ResourceStore resourceStore = new FileResourceStore(desafioUsuario.getDiretorioDeDesafiosDoUsuario());
		MemoryResourceReader leitorDeCodigo = new MemoryResourceReader();
		leitorDeCodigo.add("Teste", desafioUsuario.getCodigo());
		CompilationResult resultadoDaCompilacao = javaCompiler.compile(new String[] {"Teste"}, leitorDeCodigo, resourceStore);
		if (resultadoDaCompilacao.getErrors().length != 0) {
			Erros erros = parseProblems(resultadoDaCompilacao.getErrors());
			desafioUsuario.setErros(erros);
			throw new ErroCompilacaoException();
		}
	}

	private Erros parseProblems(CompilationProblem[] errors) {
		Erros erros = new Erros();
		for (CompilationProblem compilationProblem : errors) {
			Erro erro = criarErroAPartirDeCompilationProblem(compilationProblem);
			erros.adicionarErro(erro);
		}
		return erros;
	}

	private Erro criarErroAPartirDeCompilationProblem(CompilationProblem compilationProblem) {
		Erro erro = new Erro();
		erro.setNomeArquivo(compilationProblem.getFileName());
		erro.setPosicaoErro(new PosicaoErro(compilationProblem.getStartLine(),compilationProblem.getEndLine(), compilationProblem.getStartColumn(), compilationProblem.getEndColumn()));
		return erro;
	}


}
