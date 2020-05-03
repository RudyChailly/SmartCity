package com.example.smartcity.ui.commerces.liste;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.commerce.CommerceAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;

public class CommercesListeFragment extends Fragment {

    private ArrayList<Commerce> commerces;
    private ListView listView_commerces;
    private CommerceAdapter commerceAdapter;

    public CommercesListeFragment(){
        commerces = new ArrayList<Commerce>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commerces_liste, container, false);
        listView_commerces = (ListView) view.findViewById(R.id.liste_commerces);
        commerceAdapter = new CommerceAdapter(getContext().getApplicationContext(), commerces);
        listView_commerces.setAdapter(commerceAdapter);
        getCommerces();
        return view;
    }

    public void getCommerces() {
        ArrayList<Commerce> commercesRecuperees = ((MainActivity)getActivity()).getCommerces();
        if (commercesRecuperees == null) {
            commerces = new ArrayList<Commerce>();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(requestCommerces());
        }
        else {
            if (commerceAdapter.getCount() == 0) {
                this.commerces = commercesRecuperees;
                commerceAdapter.addAll(commercesRecuperees);
                commerceAdapter.notifyDataSetChanged();
            }
        }
    }

    public JsonArrayRequest requestCommerces() {
        String url = "http://10.0.2.2:8888/utilisateurs/0/commerces";
        JsonArrayRequest commercesRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Commerce commerce = new Commerce(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("adresse"));
                                commerceAdapter.add(commerce);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        commerceAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERREUR", error.toString());
                    }
                }
        );
        return commercesRequest;
    }

}
