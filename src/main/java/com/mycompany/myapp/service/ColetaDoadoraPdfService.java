package com.mycompany.myapp.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mycompany.myapp.service.dto.DoadoraColetasProjection;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ColetaDoadoraPdfService {

    private static final Logger log = LoggerFactory.getLogger(ColetaDoadoraPdfService.class);

    public byte[] gerarRelatorioPorDoadora(
        List<DoadoraColetasProjection> coletas,
        String nomeDoadora,
        LocalDate dataInicio,
        LocalDate dataFim
    ) throws DocumentException {
        log.debug("Gerando relatório de coletas para doadora: {}", nomeDoadora);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);

        document.open();

        // Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Relatório de Coletas - Doadora", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Dados da doadora e período
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        document.add(new Paragraph("Nome da Doadora: " + nomeDoadora, infoFont));
        if (dataInicio != null && dataFim != null) {
            document.add(new Paragraph("Período: " + dataInicio.format(formatter) + " até " + dataFim.format(formatter), infoFont));
        } else {
            document.add(new Paragraph("Período: Não especificado", infoFont));
        }
        document.add(new Paragraph("Quantidade de coletas: " + coletas.size(), infoFont));
        document.add(Chunk.NEWLINE);

        // Tabela
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = { 2f, 2f, 2f, 3f, 3f };
        table.setWidths(columnWidths);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(52, 73, 94);

        String[] headers = { "Data Coleta", "Volume (ml)", "Status", "Local", "Telefone" };

        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
        boolean alternate = false;

        for (DoadoraColetasProjection c : coletas) {
            BaseColor rowColor = alternate ? new BaseColor(245, 245, 245) : BaseColor.WHITE;
            alternate = !alternate;

            addCell(table, c.getDataColeta() != null ? c.getDataColeta().format(formatter) : "-", cellFont, rowColor);
            addCell(table, String.valueOf(c.getVolumeMl()), cellFont, rowColor);
            addCell(table, c.getStatusColeta(), cellFont, rowColor);
            addCell(table, c.getLocalColeta(), cellFont, rowColor);
            addCell(table, c.getTelefoneDoadora(), cellFont, rowColor);
        }

        document.add(table);

        // Rodapé
        Paragraph footer = new Paragraph(
            "Documento gerado em: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, BaseColor.GRAY)
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20f);
        document.add(footer);

        document.close();
        log.info("Relatório de coletas da doadora gerado com sucesso");
        return baos.toByteArray();
    }

    private void addCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "-", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5f);
        cell.setBackgroundColor(bgColor);
        table.addCell(cell);
    }
}
