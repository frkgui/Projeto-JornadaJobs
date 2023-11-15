package com.jornada.jobapi.storage;

import com.jornada.jobapi.service.VagasService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Component
public class Disco {

    @Value("${job.disco.raiz}")
    private String raiz;

    @Value("${job.disco.diretorio-curriculo}")
    private String diretorioCurriculo;


    public String salvarCurriculo(MultipartFile curriculo) {

        Path diretorioPath = Path.of(this.raiz, this.diretorioCurriculo);

        if (!Files.exists(diretorioPath)) {
            try {
                Files.createDirectories(diretorioPath);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao criar o diretório", e);
            }
        }

        Path arquivoPath = diretorioPath.resolve(Objects.requireNonNull(curriculo.getOriginalFilename()));

        if (curriculo.isEmpty()) {
            throw new RuntimeException("Arquivo do currículo inválido");
        }

        try {
//            curriculo.transferTo(arquivoPath.toFile());
            curriculo.transferTo(arquivoPath);
            System.out.println("Arquivo salvo com sucesso");
            return (raiz + diretorioCurriculo + curriculo.getOriginalFilename());

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o curriculo", e);
        }


    }
}
