package com.example.asaditasgourmet.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.example.asaditasgourmet.modelo.Cart;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    //    invoiceAndEstimate.exdb
    public static final String DATABASE_NAME = "asaditasgourmet.exdb";
    public static final int DATABASE_VERSION = 1;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {

        sQLiteDatabase.execSQL("CREATE TABLE " + Cart.TABLE_NAME + "("
                + Cart.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Cart.COLUMN_FOTO + " TEXT,"
                + Cart.COLUMN_PRECIO + " TEXT, "
                + Cart.COLUMN_PRODUCTO + " TEXT, "
                + Cart.COLUMN_CANTIDAD + " TEXT, "
                + Cart.COLUMN_SUBTOTAL + " TEXT, "
                + Cart.COLUMN_CATEGORIA + " TEXT, "
                + Cart.COLUMN_FKCATEGORIA + " TEXT "
                + ")");
    }





    public Cursor getAllCart(Context context) {
        Cursor cursor = null;
        List<Cart> imgs = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+Cart.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }




    public void deleteCart(int item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Cart.TABLE_NAME, Cart.COLUMN_ID + " = ?",
                new String[]{String.valueOf(item)});
        db.close();
    }

    public int getCountCart() {
        int mx=0;
        try{
            String countQuery = "SELECT COUNT(id) AS id FROM "+Cart.TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, new String[]{});
            if(cursor != null)
                if(cursor.moveToFirst())
                {
                    mx = cursor.getInt(0);
                }
            cursor.close();
            return mx;
        }catch(Exception e){
            return 0;
        }
    }



    public int getSumCart() {
        int mx=0;
        try{
            String countQuery = "SELECT SUM(subtotal) AS total FROM "+Cart.TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, new String[]{});
            if(cursor != null)
                if(cursor.moveToFirst())
                {
                    mx = cursor.getInt(0);
                }
            cursor.close();
            return mx;
        }catch(Exception e){
            return 0;
        }
    }




    public long insertCart(Context context, String foto, String precio, String producto, String cantidad, String  subtotal, String  categoria, String fkcategoria) {
        long id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Cart.COLUMN_FOTO, foto);
        values.put(Cart.COLUMN_PRECIO, precio);
        values.put(Cart.COLUMN_PRODUCTO, producto);
        values.put(Cart.COLUMN_CANTIDAD, cantidad);
        values.put(Cart.COLUMN_SUBTOTAL, subtotal);
        values.put(Cart.COLUMN_CATEGORIA, categoria);
        values.put(Cart.COLUMN_FKCATEGORIA, fkcategoria);
        id = db.insert(Cart.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // FIN DATOS PRODUCTO


    public void deleteAllCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+Cart.TABLE_NAME);
        db.close();
    }

    @SuppressLint("Range")
    public List<Cart> getAllCart() {
        List<Cart> imgs = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+Cart.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Cart img = new Cart();
                img.setId(cursor.getInt(cursor.getColumnIndex(Cart.COLUMN_ID)));
                img.setFoto(cursor.getString(cursor.getColumnIndex(Cart.COLUMN_FOTO)));
                img.setPrecio(cursor.getString(cursor.getColumnIndex(Cart.COLUMN_PRECIO)));
                img.setProducto(cursor.getString(cursor.getColumnIndex(Cart.COLUMN_PRODUCTO)));
                img.setCantidad(cursor.getString(cursor.getColumnIndex(Cart.COLUMN_CANTIDAD)));
                img.setSubtotal(cursor.getString(cursor.getColumnIndex(Cart.COLUMN_SUBTOTAL)));
                img.setCategoria(cursor.getString(cursor.getColumnIndex(Cart.COLUMN_CATEGORIA)));
                img.setFkCategoria(cursor.getString(cursor.getColumnIndex(Cart.COLUMN_FKCATEGORIA)));
                imgs.add(img);
            } while (cursor.moveToNext());
        }
        db.close();
        return imgs;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }
}
