package Cenarios;

import Bases.FacilitadorFuncoes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RealizarCompraOffusAvistaChipAprovada {
    FacilitadorFuncoes facilit = new FacilitadorFuncoes ();
    @Before
    public void setUp(){
        facilit.getChrome("https://10.113.3.24/PRIME4/TSPrimeRoot/Default.aspx");
        facilit.efetuarLogin("cl7496_43947451873", "Cog@2021");
        facilit.alterarIdioma();
    }

    @Test
    public void validarTransacao() throws Exception{
        facilit.opcaoatendimentoCliente ();
        facilit.buscaPorCartao ();
        facilit.acessarTransacaoAprovadaAutorizador ();
    }

    @After
    public void tearDown(){
        facilit.driver.close ();

    }
}
