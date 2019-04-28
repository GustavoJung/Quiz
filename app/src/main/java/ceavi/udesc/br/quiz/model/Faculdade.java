package ceavi.udesc.br.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class Faculdade {
    private String sigla;
    private int pontuacao;
    private Academico academicos;

    public Faculdade() {
    }
    public String getSigla() { return sigla; }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Academico getAcademicos() {
        return academicos;
    }

    public void setAcademicos(Academico academicos) {
        this.academicos = academicos;
    }
}
