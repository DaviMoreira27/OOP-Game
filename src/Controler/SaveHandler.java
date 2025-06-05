package Controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Modelo.BichinhoVaiVemHorizontal;
import Modelo.BichinhoVaiVemVertical;
import Modelo.Caveira;
import Modelo.Chaser;
import Modelo.Hero;
import Modelo.Personagem;
import Modelo.PersonagemDTO;
import Modelo.ZigueZague;
import exceptions.GameException;
import exceptions.LoadGameException;

public class SaveHandler {

    public static void salvarJogo(ArrayList<Personagem> faseAtual) {
        try {
            Gson gson = new GsonBuilder().create();
            List<PersonagemDTO> dtos = converterParaDTOs(faseAtual);
            String json = gson.toJson(dtos);

            String currentPath = System.getProperty("user.dir");

            File savesDir = new File(currentPath, "saves");
            if (!savesDir.exists()) {
                boolean created = savesDir.mkdirs();
                if (!created) {
                    System.err.println("Erro ao criar a pasta 'saves'");
                    return;
                }
            }

            File outputFile = new File(savesDir, "savegame.zip");

            FileOutputStream fos = new FileOutputStream(outputFile);
            ZipOutputStream zos = new ZipOutputStream(fos, StandardCharsets.UTF_8);

            ZipEntry entry = new ZipEntry("fase.json");
            zos.putNextEntry(entry);

            Writer writer = new OutputStreamWriter(zos, StandardCharsets.UTF_8);
            writer.write(json);
            writer.flush();

            zos.closeEntry();
            writer.close();
            zos.close();

            System.out.println("Jogo salvo com sucesso em: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar o jogo.");
        }
    }

    public static ArrayList<Personagem> carregarJogo() {
        ArrayList<Personagem> faseAtual = new ArrayList<>();

        try {
            // TODO: Permitir que o usuario escolha aonde está localizado o arquivo
            String currentPath = System.getProperty("user.dir");
            File savesDir = new File(currentPath, "saves");
            File inputFile = new File(savesDir, "savegame.zip");

            if (!inputFile.exists()) {
                throw new LoadGameException.NoSaveFileFound();
            }

            FileInputStream fis = new FileInputStream(inputFile);
            ZipInputStream zis = new ZipInputStream(fis, StandardCharsets.UTF_8);
            ZipEntry entry;

            StringBuilder jsonBuilder = new StringBuilder();
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("fase.json")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis, StandardCharsets.UTF_8));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }
                    zis.closeEntry();
                    reader.close();
                    break;
                }
            }
            zis.close();

            String json = jsonBuilder.toString();
            Gson gson = new GsonBuilder().create();
            Type listType = new TypeToken<List<PersonagemDTO>>() {
            }.getType();
            List<PersonagemDTO> dtos = gson.fromJson(json, listType);

            for (PersonagemDTO d : dtos) {
                System.out.println(d.classe);
            }

            for (PersonagemDTO dto : dtos) {
                Personagem p = criarPersonagemFromDTO(dto);
                if (p != null) {
                    faseAtual.add(p);
                }
            }

            System.out.println("Jogo carregado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();

            if (e instanceof GameException) {
                System.out.println(e.getMessage());
            }

            System.out.println("Erro ao carregar o jogo.");
        }

        return faseAtual;
    }

    public static List<PersonagemDTO> converterParaDTOs(List<Personagem> personagens) {
        List<PersonagemDTO> dtos = new ArrayList<>();
        for (Personagem p : personagens) {
            dtos.add(new PersonagemDTO(p));
        }
        return dtos;
    }

    public static Personagem criarPersonagemFromDTO(PersonagemDTO dto) {
        Personagem p = null;

        switch (dto.classe) {
            case "Hero":
                p = new Hero(dto.image, dto.dano, dto.vida, true);
                break;
            case "ZigueZague":
                p = new ZigueZague(dto.image, dto.dano, dto.vida, true);
                break;
            case "BichinhoVaiVemHorizontal":
                p = new BichinhoVaiVemHorizontal(dto.image, dto.dano, dto.vida, true);
                break;
            case "BichinhoVaiVemVertical":
                p = new BichinhoVaiVemVertical(dto.image, dto.dano, dto.vida, true);
                break;
            case "Caveira":
                p = new Caveira(dto.image, dto.dano, dto.vida, true);
                break;
            case "Chaser":
                p = new Chaser(dto.image, dto.dano, dto.vida, true);
                break;
            default:
                System.out.println("Classe desconhecida: " + dto.classe);
        }

        if (p != null) {
            p.setPosicao(dto.linha, dto.coluna);
            p.setbTransponivel(dto.transponivel);
        }

        return p;
    }

}
