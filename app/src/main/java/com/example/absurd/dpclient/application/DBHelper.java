package com.example.absurd.dpclient.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.absurd.dpclient.containers.ActivContainer;
import com.example.absurd.dpclient.containers.DocContainer;
import com.example.absurd.dpclient.containers.OTaskContainer;
import com.example.absurd.dpclient.containers.TaskContainer;

import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/com.example.absurd.dpclient/databases/";
    private static String DB_NAME = "base.db3";
    private static final int SCHEMA = 1; // версия базы данных

    public SQLiteDatabase database;
    private Context myContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
    }

    public void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH + DB_NAME);
            if (!file.exists()) {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH + DB_NAME;
                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);
                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        }
        catch(IOException ex){
        }
    }

    public void open() throws SQLException {
        String path = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    public void addTask(TaskContainer task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Code",task.getCode());
        values.put("Name",task.getTaskName());
        values.put("Kontracter",task.getContractor());
        values.put("Date",task.getDate());
        values.put("Ispolnitel",task.getExecutor());
        values.put("Complite",task.getComplite());
        db.insert("Task",null,values);
    }

    public int updateTask(TaskContainer task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Code",task.getCode());
        values.put("Name",task.getTaskName());
        values.put("Kontracter",task.getContractor());
        values.put("Date",task.getDate());
        values.put("Ispolnitel",task.getExecutor());
        values.put("Complite",task.getComplite());

        return db.update("Task", values, "Code = ?", new String[]{task.getCode()});
    }

    public ArrayList addOTasks(TaskContainer Otask){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList tmp = new ArrayList();
        ContentValues values = new ContentValues();
        //Log.e("Otask","Otask: " + Otask.getOtasks());
        for(OTaskContainer ot: Otask.getOtasks()){
            //Log.e("O","\ncodeTask: "  + ot.getCodeTask() + "\ncodeActiv: " + ot.getCodeActiv() +"\nname: " + ot.getOpisanie());
            //Log.e("Otask", ot.toString());
            values.put("name",ot.getOpisanie());
            values.put("codeActiv",ot.getCodeActiv());
            try {
                values.put("codeTask",new String(ot.getCodeTask().getBytes(),"Cp1251"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            tmp.add(db.insert("DescriptionTask",null,values));
        }
        return tmp;
    }

    public ArrayList updateOTasks(TaskContainer Otask){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("DescriptionTask","codeTask = ?", new String[]{Otask.getCode()});
        ArrayList tmp = new ArrayList();
        tmp =   addOTasks(Otask);
        return tmp;
    }

    public void addActiv(ActivContainer activ){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number",activ.getCode());
        values.put("name",activ.getName());
        values.put("shtrihCode",activ.getShtrihCode());
        values.put("photo",activ.getPhoto());
        db.insert("activ",null,values);
    }

    public int updateActiv(ActivContainer activ){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number",activ.getCode());
        values.put("name",activ.getName());
        values.put("shtrihCode",activ.getShtrihCode());
        values.put("photo",activ.getPhoto());
        return db.update("activ", values, "number = ?", new String[]{activ.getCode()});
    }

    public ArrayList<TaskContainer> getAllTasks(String code){
        ArrayList<TaskContainer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(code!=null)
            cursor = db.query("Task",null,"Code = ?",new String[]{code},null,null,null);
        else
            cursor = db.query("Task",null,null,null,null,null,null);
        ArrayList<OTaskContainer> oTaskContainers = new ArrayList<>();
        int count = 0;
        if(cursor.moveToFirst()){
            oTaskContainers.clear();
            Cursor cursorOTasks;
            do {
                if(code!=null)
                    cursorOTasks = db.query("DescriptionTask",null,"codeTask = ?",new String[] {code},null,null,null);
                else
                    cursorOTasks = db.query("DescriptionTask",null,null,null,null,null,null);
                if(cursorOTasks.moveToFirst()){
                    do {
                        OTaskContainer otask = new OTaskContainer(
                                cursorOTasks.getInt(cursorOTasks.getColumnIndex("code")),
                                cursorOTasks.getString(cursorOTasks.getColumnIndex("codeTask")),
                                cursorOTasks.getString(cursorOTasks.getColumnIndex("codeActiv")),
                                cursorOTasks.getString(cursorOTasks.getColumnIndex("name"))
                        );
                        oTaskContainers.add(otask);
                        count++;
                    }while (cursorOTasks.moveToNext());
                }
                TaskContainer container = new TaskContainer(
                        cursor.getString(cursor.getColumnIndex("Code")),
                        cursor.getString(cursor.getColumnIndex("Name")),
                        cursor.getString(cursor.getColumnIndex("Kontracter")),
                        cursor.getString(cursor.getColumnIndex("Date")),
                        cursor.getString(cursor.getColumnIndex("Ispolnitel")),
                        cursor.getString(cursor.getColumnIndex("Complite")),
                        new ArrayList(oTaskContainers)
                );
                list.add(container);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<ActivContainer> getAllActiv(){
        ArrayList<ActivContainer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Activ",null,null,null,null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                do {
                    ActivContainer container = new ActivContainer(
                            cursor.getString(cursor.getColumnIndex("number")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("typeActiv")),
                            cursor.getString(cursor.getColumnIndex("shtrihCode")),
                            cursor.getString(cursor.getColumnIndex("photo"))
                    );
                    list.add(container);
                }while (cursor.moveToNext());
            }
        }
        return list;
    }
    public String getIP(){
        String ip=null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Setting",null,null,null,null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                ip = cursor.getString(cursor.getColumnIndex("ip"))+":"+cursor.getString(cursor.getColumnIndex("port"));
            }
        }
        return ip;
    }
    public int saveIP(String ip, String port){
        SQLiteDatabase db = this.getWritableDatabase();
        int id=1;
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("port",port);
        values.put("ip",ip);
        return db.update("Setting", values, "id = ?",new String[]{"1"});
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Activ",null,null);
        db.delete("Task",null,null);
        db.delete("Documents",null,null);
    }

    public void addDocument(DocContainer doc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codeDoc",doc.getCodeDoc());
        values.put("avtorDoc",doc.getAvtorDoc());
        values.put("messageDoc",doc.getMessageDoc());
        db.insert("Documents",null,values);
    }

    public int updateDocument(DocContainer doc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codeDoc",doc.getCodeDoc());
        values.put("avtorDoc",doc.getAvtorDoc());
        values.put("messageDoc",doc.getMessageDoc());
        return db.update("Documents", values, "codeDoc = ?", new String[]{doc.getCodeDoc()});
    }

    public ArrayList<DocContainer> getAllDocuments() {
        ArrayList<DocContainer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Documents",null,null,null,null,null,null);
        if(cursor.getCount()!=-1){
            if(cursor.moveToFirst()){
                do {
                    DocContainer container = new DocContainer(
                            cursor.getString(cursor.getColumnIndex("codeDoc")),
                            cursor.getString(cursor.getColumnIndex("avtorDoc")),
                            cursor.getString(cursor.getColumnIndex("messageDoc"))
                    );
                    list.add(container);
                }while (cursor.moveToNext());
            }
        }
        return list;
    }
}
