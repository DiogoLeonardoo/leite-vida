package com.mycompany.myapp.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.myapp.service.dto.ColetaDTO;
import com.mycompany.myapp.service.dto.DoadoraDTO;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

@Service
public class ComprovanteService {

    public byte[] gerarPdfColeta(ColetaDTO coleta) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // Definindo fontes
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

        // Título principal com estilo
        Paragraph titulo = new Paragraph("RELATÓRIO DE COLETA DE LEITE HUMANO", titleFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(25f);
        document.add(titulo);

        // Dados da Coleta em tabela
        document.add(createSectionTitle("DADOS DA COLETA", sectionFont));
        PdfPTable coletaTable = new PdfPTable(2);
        coletaTable.setWidthPercentage(100);
        coletaTable.setSpacingAfter(20f);

        addTableRow(
            coletaTable,
            "Status:",
            coleta.getStatusColeta() != null ? coleta.getStatusColeta().toString() : "N/A",
            labelFont,
            valueFont
        );
        addTableRow(
            coletaTable,
            "Data da Coleta:",
            coleta.getDataColeta() != null ? coleta.getDataColeta().toString() : "N/A",
            labelFont,
            valueFont
        );
        addTableRow(
            coletaTable,
            "Local da Coleta:",
            coleta.getLocalColeta() != null ? coleta.getLocalColeta() : "N/A",
            labelFont,
            valueFont
        );
        addTableRow(
            coletaTable,
            "Volume (ml):",
            coleta.getVolumeMl() != null ? coleta.getVolumeMl().toString() : "N/A",
            labelFont,
            valueFont
        );
        addTableRow(
            coletaTable,
            "Temperatura (°C):",
            coleta.getTemperatura() != null ? coleta.getTemperatura().toString() : "N/A",
            labelFont,
            valueFont
        );
        addTableRow(
            coletaTable,
            "Observações:",
            coleta.getObservacoes() != null ? coleta.getObservacoes() : "Nenhuma",
            labelFont,
            valueFont
        );

        document.add(coletaTable);

        DoadoraDTO d = coleta.getDoadora();
        document.add(createSectionTitle("DADOS DA DOADORA", sectionFont));

        PdfPTable doadoraTable = new PdfPTable(3);
        doadoraTable.setWidthPercentage(100);
        doadoraTable.setWidths(new float[] { 1f, 1f, 1f });
        doadoraTable.setSpacingAfter(15f);

        addTableCell(doadoraTable, "Nome:", d.getNome() != null ? d.getNome() : "N/A", labelFont, valueFont, 3);
        addTableCell(doadoraTable, "CPF:", d.getCpf() != null ? d.getCpf() : "N/A", labelFont, valueFont, 1);
        addTableCell(doadoraTable, "Cartão SUS:", d.getCartaoSUS() != null ? d.getCartaoSUS() : "N/A", labelFont, valueFont, 1);
        addTableCell(
            doadoraTable,
            "Data Nascimento:",
            d.getDataNascimento() != null ? d.getDataNascimento().toString() : "N/A",
            labelFont,
            valueFont,
            1
        );
        addTableCell(doadoraTable, "Telefone:", d.getTelefone() != null ? d.getTelefone() : "N/A", labelFont, valueFont, 1);
        addTableCell(doadoraTable, "Profissão:", d.getProfissao() != null ? d.getProfissao() : "N/A", labelFont, valueFont, 1);
        addTableCell(
            doadoraTable,
            "Tipo Doadora:",
            d.getTipoDoadora() != null ? d.getTipoDoadora().toString() : "N/A",
            labelFont,
            valueFont,
            1
        );
        addTableCell(
            doadoraTable,
            "Local Pré-natal:",
            d.getLocalPreNatal() != null ? d.getLocalPreNatal().toString() : "N/A",
            labelFont,
            valueFont,
            3
        );

        document.add(doadoraTable);

        document.add(createSectionTitle("ENDEREÇO", sectionFont));
        PdfPTable enderecoTable = new PdfPTable(4);
        enderecoTable.setWidthPercentage(100);
        enderecoTable.setSpacingAfter(15f);

        addTableCell(enderecoTable, "CEP:", d.getCep() != null ? d.getCep() : "N/A", labelFont, valueFont, 1);
        addTableCell(enderecoTable, "Cidade:", d.getCidade() != null ? d.getCidade() : "N/A", labelFont, valueFont, 1);
        addTableCell(enderecoTable, "Estado:", d.getEstado() != null ? d.getEstado() : "N/A", labelFont, valueFont, 1);
        addTableCell(
            enderecoTable,
            "Data Registro:",
            d.getDataRegistro() != null ? d.getDataRegistro().toString() : "N/A",
            labelFont,
            valueFont,
            1
        );
        addTableCell(enderecoTable, "Endereço:", d.getEndereco() != null ? d.getEndereco() : "N/A", labelFont, valueFont, 4);

        document.add(enderecoTable);

        document.add(createSectionTitle("EXAMES", sectionFont));
        PdfPTable examesTable = new PdfPTable(5);
        examesTable.setWidthPercentage(100);
        examesTable.setSpacingAfter(15f);

        addTableCell(examesTable, "VDRL:", d.getResultadoVDRL() != null ? d.getResultadoVDRL().toString() : "N/A", labelFont, valueFont, 1);
        addTableCell(
            examesTable,
            "HBsAg:",
            d.getResultadoHBsAg() != null ? d.getResultadoHBsAg().toString() : "N/A",
            labelFont,
            valueFont,
            1
        );
        addTableCell(
            examesTable,
            "FTA-ABS:",
            d.getResultadoFTAabs() != null ? d.getResultadoFTAabs().toString() : "N/A",
            labelFont,
            valueFont,
            1
        );
        addTableCell(examesTable, "HIV:", d.getResultadoHIV() != null ? d.getResultadoHIV().toString() : "N/A", labelFont, valueFont, 1);
        addTableCell(
            examesTable,
            "Transfusão (5 anos):",
            Boolean.TRUE.equals(d.getTransfusaoUltimos5Anos()) ? "Sim" : "Não",
            labelFont,
            valueFont,
            1
        );

        document.add(examesTable);

        // Espaço flexível para empurrar assinaturas para o final
        Paragraph espacoFlexivel = new Paragraph(" ");
        espacoFlexivel.setSpacingAfter(40f); // Espaço reduzido para não criar segunda página
        document.add(espacoFlexivel);

        // Assinaturas fixadas no final da página
        PdfPTable assinaturaTable = new PdfPTable(2);
        assinaturaTable.setWidthPercentage(80);
        assinaturaTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        assinaturaTable.setSpacingBefore(10f);

        // Célula da enfermeira
        PdfPCell enfermeiraCell = new PdfPCell();
        enfermeiraCell.setBorder(Rectangle.NO_BORDER);
        enfermeiraCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        enfermeiraCell.setPaddingTop(15f);
        Paragraph linhaEnfermeira = new Paragraph("_________________________________", valueFont);
        Paragraph textoEnfermeira = new Paragraph("Assinatura da Enfermeira", labelFont);
        linhaEnfermeira.setAlignment(Element.ALIGN_CENTER);
        textoEnfermeira.setAlignment(Element.ALIGN_CENTER);
        textoEnfermeira.setSpacingBefore(3f);
        enfermeiraCell.addElement(linhaEnfermeira);
        enfermeiraCell.addElement(textoEnfermeira);
        assinaturaTable.addCell(enfermeiraCell);

        // Célula da doadora
        PdfPCell doadoraCell = new PdfPCell();
        doadoraCell.setBorder(Rectangle.NO_BORDER);
        doadoraCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        doadoraCell.setPaddingTop(15f);
        Paragraph linhaDoadora = new Paragraph("_________________________________", valueFont);
        Paragraph textoDoadora = new Paragraph("Assinatura da Doadora", labelFont);
        linhaDoadora.setAlignment(Element.ALIGN_CENTER);
        textoDoadora.setAlignment(Element.ALIGN_CENTER);
        textoDoadora.setSpacingBefore(3f);
        doadoraCell.addElement(linhaDoadora);
        doadoraCell.addElement(textoDoadora);
        assinaturaTable.addCell(doadoraCell);

        document.add(assinaturaTable);

        document.close();
        return out.toByteArray();
    }

    private Paragraph createSectionTitle(String title, Font font) {
        Paragraph section = new Paragraph(title, font);
        section.setSpacingBefore(15f);
        section.setSpacingAfter(8f);
        return section;
    }

    private void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Paragraph(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5f);
        labelCell.setBackgroundColor(new BaseColor(245, 245, 245));

        PdfPCell valueCell = new PdfPCell(new Paragraph(value != null ? value : "", valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5f);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addTableCell(PdfPTable table, String label, String value, Font labelFont, Font valueFont, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        cell.setColspan(colspan);

        Paragraph content = new Paragraph();
        content.add(new Chunk(label + " ", labelFont));
        content.add(new Chunk(value != null ? value : "", valueFont));
        cell.addElement(content);

        table.addCell(cell);
    }
}
