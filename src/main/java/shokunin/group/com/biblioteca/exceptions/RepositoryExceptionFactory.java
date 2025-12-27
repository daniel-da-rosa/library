package shokunin.group.com.biblioteca.exceptions;

import java.lang.reflect.GenericDeclaration;
import java.sql.Statement;

public class RepositoryExceptionFactory {
    /**
     * Cria exceção quando uma entidade não é encontrada
     * @param entidade Nome da entidade (Usuario,Emprestimo...)
     * @param id que foi buscado
     */

    public static RepositoryException entidadeNaoEncontrada(String entidade,Object id){
        return new EntidadeNaoEncontradaException(entidade,id);
    }

    /**Cria exception para quando for salvar o registro -- insert
    * @param entidade nome da Entidade
    * @param cause SQLException original
    */

    public static RepositoryException falhaPersistencia(String entidade, Throwable cause){
        return new FalhaPersistenciaException(entidade,cause);
    }

    /**Cria exceção para falha ao atualizar -- Update
    * @param entidade
    * @param id
    * @param cause
    **/
    public static RepositoryException falhaAtualizacao(String entidade,Object id, Throwable cause){
        return  new FalhaAtualizacaoException(entidade,id,cause);
    }

    /**Cria exceção para a falha ao remover --Delete
    * @param entidade Nome da entidade
    * @param id codigo do que foi tentado remover
    * @param cause SQLexception original
    *
    * */
    public  static RepositoryException falhaRemocao(String entidade,Object id, Throwable cause){
        return  new FalhaRemocaoException(entidade,id,cause);

    }
    /**Exceção para consultas - SELECT
    * @param entidade
    * @param cause
    */
    public static RepositoryException falhaConsulta(String entidade,Throwable cause){
        return  new FalhaConsultaException(entidade,cause);
    }

    /**
     * @param descricaoConsulta Descrição especifica para o erro
     * @param cause SQLException original
     */
    public static RepositoryException falhaConsultaDetalhada(String descricaoConsulta, Throwable cause){
        return new FalhaConsultaException(descricaoConsulta,cause,true);

    }

    /** Exceção para conexão com o bando de dados
     * @param cause
     */
    public static RepositoryException falhaConexão(Throwable cause){
        return  new ConexaoBancoDadosException(cause);
    }
}
