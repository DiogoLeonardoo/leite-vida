package com.mycompany.myapp.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.myapp.repository.ProcessamentoRepository;
import com.mycompany.myapp.service.dto.ProcessamentoProjection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessamentoPdfService {

    @Autowired
    private ProcessamentoRepository processamentoRepository;

    public byte[] gerarRelatorioPdf(LocalDate dataInicio, LocalDate dataFim) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        // Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph title = new Paragraph("Relatório de Processamento", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Período
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Paragraph subtitle = new Paragraph(
            String.format("Período: %s a %s", dataInicio.format(formatter), dataFim.format(formatter)),
            subtitleFont
        );
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);

        // Buscar dados
        List<ProcessamentoProjection> resultados = processamentoRepository.buscarPorPeriodo(dataInicio, dataFim);

        if (resultados.isEmpty()) {
            Paragraph noData = new Paragraph(
                "Nenhum registro encontrado para o período informado.",
                FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.RED)
            );
            noData.setAlignment(Element.ALIGN_CENTER);
            document.add(noData);
        } else {
            PdfPTable table = criarTabela();

            for (ProcessamentoProjection dto : resultados) {
                adicionarLinhaTabela(table, dto);
            }

            document.add(table);

            Paragraph footer = new Paragraph(
                String.format("Total de registros: %d", resultados.size()),
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY)
            );
            footer.setSpacingBefore(20);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);
        }

        document.close();
        return baos.toByteArray();
    }

    private PdfPTable criarTabela() throws DocumentException {
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = { 1f, 2f, 2.5f, 1.5f, 1.5f, 2f, 2f, 1f };
        table.setWidths(columnWidths);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
        String[] headers = {
            "ID",
            "Data Processamento",
            "Técnico Responsável",
            "Acidez Dornic",
            "Valor Calórico",
            "Resultado Análise",
            "Status",
            "Coleta ID",
        };

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(new BaseColor(52, 73, 94));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }

        return table;
    }

    private void adicionarLinhaTabela(PdfPTable table, ProcessamentoProjection dto) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);

        String[] valores = {
            dto.getIdProcessamento() != null ? dto.getIdProcessamento().toString() : "",
            dto.getDataProcessamento() != null ? dto.getDataProcessamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "",
            dto.getTecnicoResponsavel() != null ? dto.getTecnicoResponsavel() : "",
            dto.getValorAcidezDornic() != null ? dto.getValorAcidezDornic().toString() : "",
            dto.getValorCaloricoKcal() != null ? dto.getValorCaloricoKcal().toString() : "",
            dto.getResultadoAnalise() != null ? dto.getResultadoAnalise() : "",
            dto.getStatusProcessamento() != null ? dto.getStatusProcessamento() : "",
            dto.getColetaId() != null ? dto.getColetaId().toString() : "",
        };

        for (int i = 0; i < valores.length; i++) {
            PdfPCell cell = new PdfPCell(new Phrase(valores[i], cellFont));
            cell.setPadding(5);

            if (i == 0 || i == 1 || i == 3 || i == 4 || i == 7) {
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            } else {
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            }

            if (table.size() % 2 == 0) {
                cell.setBackgroundColor(new BaseColor(248, 249, 250));
            }

            table.addCell(cell);
        }
    }
}
