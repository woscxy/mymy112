package edu.dali;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.dali.image_album_show;
import androidx.annotation.Nullable;

public class getimage extends SQLiteOpenHelper{
    SQLiteDatabase db;
    private static final String TABLE_IMAGE="_image";
    private static final String TABLE_IMAGE1="_image";
    private static final String DATABASE_NAME="database.db";
    private static final int DATABASE_VERSION=1;
    private static final String  Image="image";

    public getimage(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String Query_Table="CREATE TABLE "+TABLE_IMAGE+"("+Image+")";
       db.execSQL(Query_Table);
       String SQL="create table tb_jingwei(_id int primary key,jingdu varchar(100),weidu varchar(100))";
       db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
           db.execSQL("DROP TABLE IF EXISTS "+TABLE_IMAGE);
           onCreate(db);
    }
    public long insertImage(String image){
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Image,image);
        return db.insert(TABLE_IMAGE,null,values);
    }
    public long insertjingweidu(String jingdua, String weidua){
        db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put("jingdu",jingdua);
        values.put("weidu",weidua);
        return db.insert("tb_jingwei",null,values);
    }






    public String getdata(){
        db=this.getReadableDatabase();
        String[] colums=new String[]{Image};
        Cursor cursor=db.query(TABLE_IMAGE,colums,null,null,null,null,null);
        int id=cursor.getColumnIndex(Image);
        String result="";
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            result=result+cursor.getString(id)+"\n";
        }
        db.close();
        return result;
    }
    public void deleteimage(long l){
        db=this.getWritableDatabase();
        db.delete(TABLE_IMAGE,Image+"="+l,null);
    }
//    private String image;
//    getimage(String image1){
//        this.image=image1;
//        String result="";
//        File myfile=new File("/sdcard/RouterSetup.txt");
//        try {
//            FileOutputStream fout=new FileOutputStream(myfile);
//            OutputStreamWriter myOutwriter=new OutputStreamWriter(fout);
//
//            result=result+image;
//            myOutwriter.append("This image is: \n"+result).append("\n");
//            myOutwriter.flush();
//            myOutwriter.close();
//            fout.close();
////            Toast.makeText(image_album_show.this,"Text saved!",Toast.LENGTH_SHORT).show();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    public void setImage(String image1){
//        this.image=image1;
//    }
//    public String getImage(){
//        return image;
//    }

}
