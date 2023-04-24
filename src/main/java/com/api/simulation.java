package com.api;

public class simulation {
    private String name;
    private String cpf;
    private String email;
    private int valor;
    private int parcelas;
    private Boolean seguro;

    public simulation(String name, String cpf, String email, int valor, int parcelas,
            boolean seguro) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.valor = valor;
        this.parcelas = parcelas;
        this.seguro = seguro;
    }

    public String getname() {
        return this.name;
    }

    public String getCPF() {
        return this.cpf;
    }

    public String getEmail() {
        return this.email;
    }

    public int getValor() {
        return this.valor;
    }

    public int getParcelas() {
        return this.parcelas;
    }

    public Boolean getSeguro() {
        return this.seguro;
    }
}
