package com.example.asaditasgourmet.modelo;


/**
 * Created by ravi on 20/02/18.
 */

public class Categoria {
    public static final String TABLE_NAME = "Categoria";

    public static final String COLUMN_ID = "idc";
    public static final String COLUMN_CATEGORIA = "categoria";
    public static final String COLUMN_FOTO = "foto";

    private int id;
    private String categoria;
    private String foto;

    public Categoria() {
    }

    public Categoria(int id, String cateegoria, String foto) {
        this.id = id;
        this.categoria = categoria;
        this.foto = foto;
    }

    public void setIdCategoria(int id) {
    this.id = id;
    }
    public int getIdCategoria() {
    return id;
    }

    public void setCategoria(String categoria) {
    this.categoria = categoria;
    }
    public String getCategoria() {
    return categoria;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getFoto() {
        return foto;
    }

}
