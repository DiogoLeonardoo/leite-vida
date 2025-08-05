package com.mycompany.myapp.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.myapp.repository.ColetaRepository;
import com.mycompany.myapp.service.dto.ColetaRelatorioProjection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatorioColetaPeriodoService {

    @Autowired
    private ColetaRepository coletaRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] gerarRelatorioColetas(LocalDate dataInicio, LocalDate dataFim) throws DocumentException, IOException {
        // Buscar dados da query
        List<ColetaRelatorioProjection> coletas = coletaRepository.buscarColetasPorPeriodo(dataInicio, dataFim);

        // Criar documento PDF
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Adicionar título
        adicionarTitulo(document, dataInicio, dataFim);

        // Adicionar tabela com dados
        adicionarTabelaColetas(document, coletas);

        // Adicionar resumo
        adicionarResumo(document, coletas);

        document.close();

        return baos.toByteArray();
    }

    private void adicionarTitulo(Document document, LocalDate dataInicio, LocalDate dataFim) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);

        Paragraph title = new Paragraph("RELATÓRIO DE COLETAS", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);

        Paragraph subtitle = new Paragraph(
            String.format("Período: %s a %s", dataInicio.format(DATE_FORMATTER), dataFim.format(DATE_FORMATTER)),
            subtitleFont
        );
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);

        document.add(title);
        document.add(subtitle);
    }

    private void adicionarTabelaColetas(Document document, List<ColetaRelatorioProjection> coletas) throws DocumentException {
        // Criar tabela com 8 colunas
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);

        // Definir larguras das colunas
        float[] columnWidths = { 8f, 12f, 10f, 15f, 12f, 25f, 15f, 15f };
        table.setWidths(columnWidths);

        // Adicionar cabeçalhos
        adicionarCabecalhoTabela(table);

        // Adicionar dados
        for (ColetaRelatorioProjection coleta : coletas) {
            adicionarLinhaTabela(table, coleta);
        }

        document.add(table);
    }

    private void adicionarCabecalhoTabela(PdfPTable table) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(52, 73, 94); // Azul escuro

        String[] headers = { "ID", "Data Coleta", "Volume (ml)", "Local", "Status", "Nome Doadora", "CPF", "Telefone" };

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            table.addCell(cell);
        }
    }

    private void adicionarLinhaTabela(PdfPTable table, ColetaRelatorioProjection coleta) throws DocumentException {
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);

        // ID da Coleta
        PdfPCell idCell = new PdfPCell(new Phrase(String.valueOf(coleta.getColetaId()), dataFont));
        idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        idCell.setPadding(5);
        table.addCell(idCell);

        // Data da Coleta
        PdfPCell dataCell = new PdfPCell(new Phrase(coleta.getDataColeta().format(DATE_FORMATTER), dataFont));
        dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        dataCell.setPadding(5);
        table.addCell(dataCell);

        // Volume
        PdfPCell volumeCell = new PdfPCell(new Phrase(String.valueOf(coleta.getVolumeMl()), dataFont));
        volumeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        volumeCell.setPadding(5);
        table.addCell(volumeCell);

        // Local
        PdfPCell localCell = new PdfPCell(new Phrase(coleta.getLocalColeta(), dataFont));
        localCell.setPadding(5);
        table.addCell(localCell);

        // Status
        PdfPCell statusCell = new PdfPCell(new Phrase(coleta.getStatusColeta(), dataFont));
        statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusCell.setPadding(5);

        // Colorir célula baseado no status
        if ("CONCLUIDA".equalsIgnoreCase(coleta.getStatusColeta())) {
            statusCell.setBackgroundColor(new BaseColor(212, 237, 218)); // Verde claro
        } else if ("PENDENTE".equalsIgnoreCase(coleta.getStatusColeta())) {
            statusCell.setBackgroundColor(new BaseColor(255, 243, 205)); // Amarelo claro
        } else if ("CANCELADA".equalsIgnoreCase(coleta.getStatusColeta())) {
            statusCell.setBackgroundColor(new BaseColor(248, 215, 218)); // Vermelho claro
        }

        table.addCell(statusCell);

        // Nome da Doadora
        PdfPCell nomeCell = new PdfPCell(new Phrase(coleta.getNomeDoadora(), dataFont));
        nomeCell.setPadding(5);
        table.addCell(nomeCell);

        // CPF
        PdfPCell cpfCell = new PdfPCell(new Phrase(formatarCPF(coleta.getCpfDoadora()), dataFont));
        cpfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cpfCell.setPadding(5);
        table.addCell(cpfCell);

        // Telefone
        PdfPCell telefoneCell = new PdfPCell(new Phrase(formatarTelefone(coleta.getTelefoneDoadora()), dataFont));
        telefoneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        telefoneCell.setPadding(5);
        table.addCell(telefoneCell);
    }

    private void adicionarResumo(Document document, List<ColetaRelatorioProjection> coletas) throws DocumentException {
        document.add(new Paragraph("\n"));

        Font resumoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font dadosFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        Paragraph resumoTitle = new Paragraph("RESUMO DO PERÍODO", resumoFont);
        resumoTitle.setSpacingAfter(10);
        document.add(resumoTitle);

        // Calcular estatísticas
        int totalColetas = coletas.size();
        int volumeTotal = coletas.stream().mapToInt(c -> c.getVolumeMl() != null ? c.getVolumeMl().intValue() : 0).sum();

        long coletasCompletas = coletas.stream().filter(c -> "CONCLUIDA".equalsIgnoreCase(c.getStatusColeta())).count();

        long coletasPendentes = coletas.stream().filter(c -> "PENDENTE".equalsIgnoreCase(c.getStatusColeta())).count();

        long coletasCanceladas = coletas.stream().filter(c -> "CANCELADA".equalsIgnoreCase(c.getStatusColeta())).count();

        // Adicionar estatísticas
        document.add(new Paragraph(String.format("• Total de coletas: %d", totalColetas), dadosFont));
        document.add(new Paragraph(String.format("• Volume total coletado: %d ml", volumeTotal), dadosFont));
        document.add(new Paragraph(String.format("• Coletas concluídas: %d", coletasCompletas), dadosFont));
        document.add(new Paragraph(String.format("• Coletas pendentes: %d", coletasPendentes), dadosFont));
        document.add(new Paragraph(String.format("• Coletas canceladas: %d", coletasCanceladas), dadosFont));

        if (totalColetas > 0) {
            double mediaVolume = (double) volumeTotal / totalColetas;
            document.add(new Paragraph(String.format("• Volume médio por coleta: %.2f ml", mediaVolume), dadosFont));
        }
    }

    private String formatarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarTelefone(String telefone) {
        if (telefone == null) {
            return "";
        }

        String numeros = telefone.replaceAll("\\D", "");

        if (numeros.length() == 11) {
            return numeros.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        } else if (numeros.length() == 10) {
            return numeros.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }

        return telefone;
    }
}
