package com.example.asaditasgourmet.modelo;

/**
 * Created by ravi on 20/02/18.
 */

public class Pedido {
    public static final String TABLE_NAME = "Pedido";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_SUBTOTAL = "subtotal";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_DIRECCION = "direccion";
    public static final String COLUMN_TELEFONO = "telefono";
    public static final String COLUMN_ESTADO = "estado";

    private int id;
    private String total;
    private String subtotal;
    private String direccion;
    private String fecha;
    private String telefono;
    private String estado;


    public Pedido() {
    }

    public Pedido(int id, String total, String subtotal, String direccion,
                    String fecha,
                    String estado,
                    String telefono
    ) {
        this.id = id;
        this.total = total;
        this.subtotal = subtotal;
        this.direccion = direccion;
        this.fecha = fecha;
        this.estado = estado;
        this.telefono = telefono;
    }

    public void setId(int id) {
    this.id = id;
    }
    public int getId() {
    return id;
    }

    public void setTotal(String total) {
    this.total = total;
    }
    public String getTotal() {
    return total;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
    public String getSubtotal() {
        return subtotal;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getDireccion() {
        return direccion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getFecha() {
        return fecha;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getEstado() {
        return estado;
    }

    public void serTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getTelefono() {
        return telefono;
    }
}
