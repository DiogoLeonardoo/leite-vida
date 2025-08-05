package com.mycompany.myapp.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mycompany.myapp.domain.Estoque; // ajuste o pacote
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EstoquePdfService {

    private static final Logger log = LoggerFactory.getLogger(EstoquePdfService.class);

    public byte[] gerarRelatorioEstoque(List<Estoque> estoques) throws DocumentException {
        log.debug("Gerando relatório de Estoque");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate()); // rotacionado para melhor largura
        PdfWriter.getInstance(document, baos);

        document.open();

        // Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Relatório de Estoque - Banco de Leite", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Tabela
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = { 1.5f, 2f, 2f, 2f, 2f, 2f, 2f, 3f, 2f };
        table.setWidths(columnWidths);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(52, 73, 94);

        String[] headers = {
            "ID",
            "Data Produção",
            "Data Validade",
            "Tipo Leite",
            "Classificação",
            "Volume Total (ml)",
            "Volume Disponível (ml)",
            "Local Armazenamento",
            "Status Lote",
        };

        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
        boolean alternate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Estoque e : estoques) {
            BaseColor rowColor = alternate ? new BaseColor(245, 245, 245) : BaseColor.WHITE;
            alternate = !alternate;

            addCell(table, String.valueOf(e.getId()), cellFont, rowColor);
            addCell(table, e.getDataProducao() != null ? e.getDataProducao().format(formatter) : "-", cellFont, rowColor);
            addCell(table, e.getDataValidade() != null ? e.getDataValidade().format(formatter) : "-", cellFont, rowColor);
            addCell(table, e.getTipoLeite() != null ? e.getTipoLeite().toString() : "-", cellFont, rowColor);
            addCell(table, e.getClassificacao() != null ? e.getClassificacao().toString() : "-", cellFont, rowColor);
            addCell(table, String.valueOf(e.getVolumeTotalMl()), cellFont, rowColor);
            addCell(table, String.valueOf(e.getVolumeDisponivelMl()), cellFont, rowColor);
            addCell(table, e.getLocalArmazenamento() != null ? e.getLocalArmazenamento() : "-", cellFont, rowColor);
            addCell(table, e.getStatusLote() != null ? e.getStatusLote().toString() : "-", cellFont, rowColor);
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

        log.info("Relatório de estoque gerado com sucesso");
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
