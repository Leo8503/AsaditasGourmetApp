package com.example.asaditasgourmet.modelo;

/**
 * Created by ravi on 20/02/18.
 */

public class Producto {
    public static final String TABLE_NAME = "Producto";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_FOTO = "foto";
    public static final String COLUMN_ESTADO = "estado";
    public static final String COLUMN_ATEGORIA = "categoria";
    public static final String COLUMN_FKCATEGORIA = "fkcategoria";

    private int id;
    private String nombre;
    private String precio;
    private String descripcion;
    private String foto;
    private String estado;
    private String categoria;
    private String fkcategoria;


    public Producto() {
    }

    public Producto(int id, String nombre, String precio, String descripcion,
                    String foto, String estado, String fkcategoria, String categoria
    ) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.foto = foto;
        this.estado = estado;
        this.fkcategoria = fkcategoria;
        this.categoria = categoria;
    }

    public void setIdProducto(int id) {
    this.id = id;
    }
    public int getIdProducto() {
    return id;
    }

    public void setProducto(String nombre) {
    this.nombre = nombre;
    }
    public String getProducto() {
    return nombre;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
    public String getPrecio() {
        return precio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getDescripcion() {
        return descripcion;
    }


    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getFoto() {
        return foto;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getEstado() {
        return estado;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getCategoria() {
        return categoria;
    }


    public void setFkCategoria(String fkcategoria) {
        this.fkcategoria = fkcategoria;
    }
    public String getFkCategoria() {
        return fkcategoria;
    }

}
