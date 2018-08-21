import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConsultaCreditoRotativo {
    FacilitadorFuncoes facilit = new FacilitadorFuncoes();

    @Before
    public void setUp() {
        facilit.getChrome("https://10.113.3.24/PRIME4/TSPrimeRoot/Default.aspx");
        facilit.efetuarLogin("cl7496_43947451873", "Cog@2021");
        facilit.alterarIdioma();
    }

    @Test
    public void ConsultarCartao() throws Exception{
        facilit.opcaoatendimentoCliente ();
        facilit.buscaPorCartao ();
        facilit.localizarSaldoDoCreditoCartao ();
    }

    @After
    public  void tearDown(){
        facilit.driver.close ();
    }
}
