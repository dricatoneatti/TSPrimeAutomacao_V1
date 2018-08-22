import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class FacilitadorFuncoes extends DriverPage {
    //Nessa classe, será armazenado os métodos para serem chamados da classe que será realizado o teste

    public WebElement getElement(String id) {
        return driver.findElement ((By.id (id)));
    }

    public WebElement getElementXpath(String xpath) {
        return driver.findElement ((By.xpath (xpath)));
    }

    public void efetuarLogin(String usuario, String senha) {
        getElement ("ctl00_MainPlaceHolder_txtUserName").click ();
        getElement ("ctl00_MainPlaceHolder_txtUserName").sendKeys (usuario);
        getElement ("ctl00_MainPlaceHolder_txtPassword").click ();
        getElement ("ctl00_MainPlaceHolder_txtPassword").sendKeys (senha);
        getElement ("ctl00_MainPlaceHolder_btnLogon").click ();

        // Condição caso apareça a mensagem "Session Alredy Active"
        if (getElementXpath ("//div[@id='maincontent']") != null) {
            getElement ("ctl00_MainPlaceHolder_btnTerminateSession").click ();
        }
        //TESTAR
        getElement ("ctl00_MainPlaceHolder_btnGo").click ();

    }

    public void alterarIdioma() {
        driver.switchTo ().frame ("bannerFrame");
        getElementXpath ("//select[@name='ddlLanguage']").click ();
        getElementXpath ("//option[@value='pt-BR']").click ();
    }

    public void buscaPorCartao() {
        //Acessar o campo Card Number
        driver.manage ().timeouts ().implicitlyWait (10, TimeUnit.SECONDS);
        driver.switchTo ().frame ("contentFrame").switchTo ().frame ("applicationFrame").switchTo ().frame ("CardLeftPage");
        getElementXpath ("//div[@id='ctlSearch_update']//input[@name='ctlSearch$txtfind']").click ();


    }

    public void opcaoatendimentoCliente() {
        //Selecionar a opção "Atendimento ao Cliente"
        driver.manage ().timeouts ().implicitlyWait (20, TimeUnit.SECONDS);
        driver.switchTo ().frame ("contentFrame");
        getElementXpath ("//li[2][text()='Atendimento ao Cliente']").click ();
        driver.switchTo ().defaultContent ();
    }

    public void localizarSaldoDoCreditoCartao() throws Exception {
        // Criar um Array List que receba os dados da planilha do Excel em que é enviada para a classe "LerExcel" a PRIMEIRA COLUNA com os dados (0).
        ArrayList<String> lista = LerExcel.leituraCartao (0);


        //Estrutura de repetição que realiza o teste para cada cartão
        for (int i = 1; i < lista.size (); i++) {
            //Acessar a caixa de texto para pesquisa por cartão e inserir os números extraídos do excel
            getElementXpath ("//div[@id='ctlSearch_update']//input[@name='ctlSearch$txtfind']").sendKeys (lista.get (i));
            getElementXpath ("//a[@id='ctlSearch_btnFind']").click ();

            //Alterar o frame
            driver.switchTo ().frame ("contentFrame").switchTo ().frame ("applicationFrame").switchTo ().frame ("CardMainPage");

            String saldo = getElementXpath ("//div[@class='col-one']//span[@id='MainPageAccUC_AccBalance']").getText ();

            //TESTAR
            System.out.println ("Saldo do crédito do cartão " + lista.get (i) +  ": " + saldo );
            driver.switchTo ().defaultContent ();

            //alteração de Frame
            driver.switchTo ().frame ("contentFrame").switchTo ().frame ("applicationFrame").switchTo ().frame ("CardLeftPage");
            //Limpar a caixa de texto para nova busca
            getElementXpath ("//div[@id='ctlSearch_update']//input[@name='ctlSearch$txtfind']").clear ();
            driver.switchTo ().defaultContent ();


            buscaPorCartao ();
            if (lista == null) {
                System.out.println ("Todos os créditos dos cartões da lista, foram consultados");
            }
        }

    }

    public void acessarTransacaoAprovadaAutorizador() throws Exception {

        // Criar um Array List que receba os dados da planilha do Excel em que é enviada para a classe "LerExcel" a PRIMEIRA COLUNA com os dados (0).
        ArrayList<String> lista = LerExcel.leituraCartao (0);

        //Estrutura de repetição que realiza o teste para cada cartão
        for (int i = 1; i < lista.size (); i++) {

            driver.switchTo ().frame ("contentFrame").switchTo ().frame ("applicationFrame").switchTo ().frame ("CardLeftPage");
            //Acessar a caixa de texto para pesquisa por cartão e inserir os números extraídos do excel
            getElementXpath ("//div[@id='ctlSearch_update']//input[@name='ctlSearch$txtfind']").sendKeys (lista.get (i));
            getElementXpath ("//a[@id='ctlSearch_btnFind']").click ();

            driver.manage ().timeouts ().implicitlyWait (5, TimeUnit.SECONDS);

            //Alterar o frame para: CardMainPage
            driver.switchTo ().frame ("contentFrame").switchTo ().frame ("applicationFrame").switchTo ().frame ("CardMainPage");
            //Imprime o saldo de crédito do cartão pesquisado
            String saldo = getElementXpath ("//div[@class='col-one']//span[@id='MainPageAccUC_AccBalance']").getText ();
            System.out.println ("Saldo do crédito do cartão: " + saldo);

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.xpath ("//div[@id='MainPageCardxUC_CardPanel']//a[@id='MainPageCardxUC_AuthDetail']")));
            //Acessar o Autorizador

            getElementXpath ("//div[@id='MainPageCardxUC_CardPanel']//a[@id='MainPageCardxUC_AuthDetail']").click ();
            getElementXpath ("//td[@id=\"MenuSite\"]//input[@value=\"Atividade\"]").click ();

            //Validar o código de retorno da transação
            String transacaoRetorno = getElementXpath ("//table[@class=\"grid\"]//tr[2]//td[9]").getText ();

            switch (transacaoRetorno) {
                case "00":
                    transacaoRetorno = "A transação foi aprovada com sucesso";
                    break;
                default:
                    transacaoRetorno = "A transação não foi aprovada, valide outro cartão (massa ruim)";
            }

            System.out.println (transacaoRetorno);

            driver.switchTo ().defaultContent ();

            //Alteração do Frame para: CardLeftPage
            driver.switchTo ().frame ("contentFrame").switchTo ().frame ("applicationFrame").switchTo ().frame ("CardLeftPage");
            //Limpar a caixa de texto para nova busca
            getElementXpath ("//div[@id='ctlSearch_update']//input[@name='ctlSearch$txtfind']").clear ();
            driver.switchTo ().defaultContent ();

            if (lista == null) {
                System.out.println ("Todos os cartões da lista foram pesquisados com sucesso");
            }
        }
        buscaPorCartao ();
    }

    public void acessarTransacaoNegadaCartaoInativoAutorizador() throws Exception{
        // Criar um Array List que receba os dados da planilha do Excel em que é enviada para a classe "LerExcel" a PRIMEIRA COLUNA com os dados (0).
        ArrayList<String> lista = LerExcel.leituraCartao (0);

        //Estrutura de repetição que realiza o teste para cada cartão
        for (int i = 1; i < lista.size (); i++) {


            //Acessar a caixa de texto para pesquisa por cartão e inserir os números extraídos do excel
            getElementXpath ("//div[@id='ctlSearch_update']//input[@name='ctlSearch$txtfind']").sendKeys (lista.get (i));
            getElementXpath ("//a[@id='ctlSearch_btnFind']").click ();

            driver.manage ().timeouts ().implicitlyWait (20, TimeUnit.SECONDS);
            //Alterar o frame para: CardMainPage
            driver.switchTo ().frame ("contentFrame").switchTo ().frame ("applicationFrame").switchTo ().frame ("CardMainPage");
            //Imprime o saldo de crédito do cartão pesquisado
            String saldo = getElementXpath ("//div[@class='col-one']//span[@id='MainPageAccUC_AccBalance']").getText ();
            System.out.println ("Saldo do crédito do cartão: " + saldo);

           //Acessar o Autorizador
            wait.until (ExpectedConditions.visibilityOfElementLocated (By.xpath ("//div[@id='MainPageCardxUC_CardPanel']//a[@id='MainPageCardxUC_AuthDetail']")));
            getElementXpath ("//div[@id='MainPageCardxUC_CardPanel']//a[@id='MainPageCardxUC_AuthDetail']").click ();

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.xpath ("//input[@name='basePage_menuContent$ctl01']")));
            getElementXpath ("//input[@name='basePage_menuContent$ctl01']").click ();

            //Validar o código de retorno da transação
            String transacaoRetorno = getElementXpath ("//table[@class='grid']//tr[2]//td[9]").getText ();

            switch (transacaoRetorno) {
                case "05":
                    transacaoRetorno = "A transação foi Negada como esperado";
                    break;
                case "14":
                    transacaoRetorno = "A transação foi Negada como esperado";
                    break;
                case "33":
                    transacaoRetorno = "A transação foi Negada como esperado";
                    break;
                case "54":
                    transacaoRetorno = "A transação foi Negada como esperado";
                    break;
                case "78":
                    transacaoRetorno = "A transação foi Negada como esperado";
                    break;
                default:
                    transacaoRetorno = "Não possui transações negadas por estar Inativo, valide outro cartão";
            }

            System.out.println (transacaoRetorno);

            driver.switchTo ().defaultContent ();

            //Alteração do Frame para: CardLeftPage
            driver.switchTo ().frame ("contentFrame").switchTo ().frame ("applicationFrame").switchTo ().frame ("CardLeftPage");
            //Limpar a caixa de texto para nova busca
            getElementXpath ("//div[@id='ctlSearch_update']//input[@name='ctlSearch$txtfind']").clear ();
            driver.switchTo ().defaultContent ();
            buscaPorCartao ();
            if (lista == null) {
                System.out.println ("Todos os cartões da lista foram pesquisados com sucesso");
            }
        }

    }
}

