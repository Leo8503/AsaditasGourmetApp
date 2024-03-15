package com.example.asaditasgourmet.modelo;


/**
 * Created by ravi on 20/02/18.
 */

public class detalle {
    public static final String TABLE_NAME = "Cart";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FOTO = "foto";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_PRODUCTO = "producto";
    public static final String COLUMN_CANTIDAD = "cantidad";
    public static final String COLUMN_SUBTOTAL = "subtotal";
    public static final String COLUMN_CATEGORIA = "categoria";


    private int id;
    private String foto;
    private String precio;
    private String producto;
    private String cantidad;
    private String subtotal;
    private String categoria;

    public detalle() {
    }

    public detalle(int id, String foto, String precio, String producto, String cantidad, String subtotal, String categoria) {
        this.id = id;
        this.foto = foto;
        this.precio = precio;
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.categoria = categoria;

    }

    public void setId(int id) {
    this.id = id;
    }
    public int getId() {
    return id;
    }

    public void setFoto(String foto) {
    this.foto = foto;
    }
    public String getFoto() {
    return foto;
    }

    public void setPrecio(String precio) {
    this.precio = precio;
    }
    public String getPrecio() {
    return precio;
    }

    public void setProducto(String producto) {
    this.producto = producto;
    }
    public String getProducto() {
    return producto;
    }

    public void setCantidad(String cantidad) {
    this.cantidad = cantidad;
    }
    public String getCantidad() {
    return cantidad;
    }

    public void setSubtotal(String subtotal) {
    this.subtotal = subtotal;
    }
    public String getSubtotal() {
    return subtotal;
    }

    public void setCategoria(String categoria) {
    this.categoria = categoria;
    }
    public String getCategoria() {
    return categoria;
    }


}
