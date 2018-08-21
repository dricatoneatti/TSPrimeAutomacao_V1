import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.ArrayList;

public class LerExcel {
    public static  ArrayList<String> leituraCartao(int numCartoes) throws Exception {

        // Localizar o caminho do arquivo Excel. (Alterar para o proprio de referencia)
        FileInputStream diretorio = new FileInputStream("C:\\Users\\640244\\Documents\\IniciativaCSF\\Cartoes.xlsx");

        // Criar a variável "wb" do tipo WorkBook.
        XSSFWorkbook wb = new XSSFWorkbook(diretorio);
        XSSFSheet aba = wb.getSheet("Sheet1");

        // Gerar o Iterator
        java.util.Iterator<Row> IteratorDeLinha = aba.iterator();

        // Instanciar a variável "list" do tipo ArrayList
        ArrayList<String> list = new ArrayList<String>();

        // Usar um loop para adicionar todos os dados da coluna do excel no Array "list
        while (IteratorDeLinha.hasNext()) { // o while continuará o loop enquanto houver uma célula preenchida.
            list.add(IteratorDeLinha.next().getCell(numCartoes).getStringCellValue());
        }
        System.out.println(list);

        return list;
    }
}


