package com.example.absurd.dpclient.application;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.absurd.dpclient.adapters.DocAdapter;
import com.example.absurd.dpclient.adapters.TaskAdapter;
import com.example.absurd.dpclient.containers.DocContainer;
import com.example.absurd.dpclient.containers.OTaskContainer;
import com.example.absurd.dpclient.containers.TaskContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RESTController {

    private ProgressDialog dialog;
    private DBHelper dbHelper;
    private Context context;
    private String tag;
    private String ip;
    private String prefix = "http://";

    public RESTController(Context context,String tag) {
        this.context = context;
        this.tag = tag;
        dbHelper = new DBHelper(context);
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        this.ip = dbHelper.getIP();

    }

    public void getTasks(final ListView listView) {
        String tag_string_req = "req_get_tasks";
        dialog.setMessage("Get tasks...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Get Tasks Response: " + response.toString());
                        hideDialog();
                        ArrayList<TaskContainer> tmp = new ArrayList<>();
                        ArrayList<OTaskContainer> otasks = new ArrayList<>();
                            JSONArray jsonarray = null;
                            try {
                                jsonarray = new JSONArray(response);
                                JSONArray arrayOTasks = new JSONArray();
                            for (int i = 0; i < jsonarray.length(); i++) {
                                otasks.clear();
                                JSONObject task = jsonarray.getJSONObject(i);
                                arrayOTasks = task.getJSONArray("otasks");
                                for (int j = 0; j < arrayOTasks.length(); j++) {
                                    JSONObject otask = arrayOTasks.getJSONObject(j);
                                    otasks.add(new OTaskContainer(0, task.getString("code"),
                                            otask.getString("codeActiv"),
                                            otask.getString("opisanie")));
                                }
                                TaskContainer taskContainer = new TaskContainer(
                                        task.getString("code"),
                                        task.getString("taskName"),
                                        task.getString("contractor"),
                                        task.getString("date"),
                                        task.getString("executor"),
                                        task.getString("complite"),
                                        new ArrayList(otasks)
                                );
                                tmp.add(taskContainer);
                            }
                            ArrayList<TaskContainer> oldTasks = new ArrayList<>();
                            oldTasks = dbHelper.getAllTasks(null);
                            for(TaskContainer t: tmp){
                                String oldTask = null;
                                for(TaskContainer oldt: oldTasks) {
                                    if(t.getCode().equals(oldt.getCode())){
                                        dbHelper.updateTask(t);
                                        dbHelper.updateOTasks(t);
                                        oldTask = oldt.getCode();
                                        break;
                                    }
                                }
                                if(oldTask==null) {
                                    dbHelper.addTask(t);
                                    dbHelper.addOTasks(t);
                                }
                            }
                            listView.setAdapter(new TaskAdapter(context, dbHelper.getAllTasks(null)));
                            listView.setDividerHeight(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Log.e(tag, error.getMessage());
                    showMessage("Connection problems");
                }
                else
                    Toast.makeText(context, "Problem with internet connection. Try again.", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "get_tasks");
                return params;
            }

        };
        Toast.makeText(context, strReq.toString(), Toast.LENGTH_SHORT).show();
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getDocuments(final ListView listView) {
        String tag_string_req = "req_get_doc";
        dialog.setMessage("Get documents...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, prefix+ip,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(tag, "Get Documents Response: " + response.toString());
                        hideDialog();
                        ArrayList<DocContainer> tmp = new ArrayList<>();
                        JSONArray jsonarray = null;

                        try {
                            jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject doc = jsonarray.getJSONObject(i);
                                DocContainer docContainer = new DocContainer(
                                        doc.getString("codeDoc"),
                                        doc.getString("avtorDoc"),
                                        doc.getString("messageDoc")
                                );
                                tmp.add(docContainer);
                            }

                            ArrayList<DocContainer> oldDoc = new ArrayList<>();
                            oldDoc = dbHelper.getAllDocuments();
                            for(DocContainer t: tmp){
                                String oldTask = null;
                                for(DocContainer oldt: oldDoc) {
                                    if(t.getCodeDoc().equals(oldt.getCodeDoc())){
                                        dbHelper.updateDocument(t);
                                        oldTask = oldt.getCodeDoc();
                                        break;
                                    }
                                }
                                if(oldTask==null) {
                                    dbHelper.addDocument(t);
                                }
                            }
                            listView.setAdapter(new DocAdapter(context, dbHelper.getAllDocuments()));
                            listView.setDividerHeight(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Log.e(tag, error.getMessage());
                    showMessage("Connection problems");
                }
                else
                    Toast.makeText(context, "Problem with internet connection. Try again.", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "get_documents");
                return params;
            }

        };
        Toast.makeText(context, strReq.toString(), Toast.LENGTH_SHORT).show();
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showMessage(String msg) {
        Toast toast = Toast.makeText(context,
                msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
        toast.show();
    }

    private void showDialog(){
        if(!dialog.isShowing()){
            dialog.show();
        }
    }

    private void hideDialog(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
