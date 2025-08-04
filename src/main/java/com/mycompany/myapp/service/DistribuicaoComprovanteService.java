package com.mycompany.myapp.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.service.dto.DistribuicaoRequestDTO;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Serviço para geração de comprovantes de distribuição em PDF.
 */
@Service
public class DistribuicaoComprovanteService {

    private static final Logger log = LoggerFactory.getLogger(DistribuicaoComprovanteService.class);

    /**
     * Gera um PDF com o comprovante de distribuição de leite materno
     *
     * @param dto      Dados da requisição de distribuição
     * @param estoque  Dados do estoque distribuído
     * @param paciente Dados do paciente que recebeu a distribuição
     * @return Array de bytes contendo o PDF gerado
     * @throws DocumentException Se houver erro na geração do PDF
     */
    public byte[] gerarComprovantePdf(DistribuicaoRequestDTO dto, Estoque estoque, Paciente paciente) throws DocumentException {
        log.debug("Gerando comprovante de distribuição em PDF para estoque: {} e paciente: {}", estoque.getId(), paciente.getId());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Comprovante de Distribuição de Leite Materno", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(30);
        document.add(title);

        // Informações da Distribuição
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph distribuicaoHeader = new Paragraph("Dados da Distribuição", sectionFont);
        distribuicaoHeader.setSpacingBefore(15);
        distribuicaoHeader.setSpacingAfter(10);
        document.add(distribuicaoHeader);

        document.add(
            new Paragraph("Data da Distribuição: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
        );
        document.add(new Paragraph("Volume Distribuído: " + dto.getVolumeDistribuidoMl() + " ml"));
        document.add(new Paragraph("Observações: " + (dto.getObservacoes() != null ? dto.getObservacoes() : "-")));
        document.add(new Paragraph(" "));

        // Informações do Estoque
        Paragraph estoqueHeader = new Paragraph("Dados do Frasco", sectionFont);
        estoqueHeader.setSpacingBefore(25);
        estoqueHeader.setSpacingAfter(10);
        document.add(estoqueHeader);

        document.add(new Paragraph("ID do Frasco (Lote): " + estoque.getId()));
        document.add(new Paragraph("Classificação: " + estoque.getClassificacao()));
        document.add(new Paragraph("Tipo de Leite: " + estoque.getTipoLeite()));
        document.add(new Paragraph(" "));

        // Informações do Paciente
        Paragraph pacienteHeader = new Paragraph("Dados do Receptor", sectionFont);
        pacienteHeader.setSpacingBefore(25);
        pacienteHeader.setSpacingAfter(10);
        document.add(pacienteHeader);

        document.add(new Paragraph("Nome do Paciente: " + paciente.getNome()));
        document.add(new Paragraph("Nome Responsável: " + paciente.getNomeResponsavel()));
        document.add(new Paragraph("Telefone do Responsável: " + paciente.getTelefoneResponsavel()));
        document.add(new Paragraph("Idade Gestacional: " + paciente.getIdadeGestacional()));
        document.add(
            new Paragraph(
                "Data de Nascimento: " +
                (paciente.getDataNascimento() != null
                        ? paciente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "-")
            )
        );

        // Tabela de assinaturas
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(40f);
        table.setSpacingAfter(40f);

        // Cabeçalhos
        PdfPCell cell1 = new PdfPCell(new Phrase("Assinatura Responsável"));
        PdfPCell cell2 = new PdfPCell(new Phrase("Assinatura Profissional"));

        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setPadding(8f);
        cell2.setPadding(8f);
        table.addCell(cell1);
        table.addCell(cell2);

        // Espaços para assinatura
        PdfPCell signCell1 = new PdfPCell();
        PdfPCell signCell2 = new PdfPCell();
        signCell1.setFixedHeight(70f);
        signCell2.setFixedHeight(70f);
        table.addCell(signCell1);
        table.addCell(signCell2);

        document.add(table);

        // Rodapé
        Paragraph footer = new Paragraph(
            "Documento gerado em: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20f);
        document.add(footer);

        document.close();

        log.info("Comprovante de distribuição gerado com sucesso");
        return baos.toByteArray();
    }
}
