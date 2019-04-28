package ceavi.udesc.br.quiz.model;

public class Academico {
    private String nome;
    private int pontuacao;
    private String faculdade;
    private String spe;
    private String email;

    public Academico() {
    }

    public String getSpe() {
        return spe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSpe(String spe) { this.spe = spe; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontuacao() { return pontuacao; }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(String faculdade) {
        this.faculdade = faculdade;
    }
}
