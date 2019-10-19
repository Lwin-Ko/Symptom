package com.prembros.symptomator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CompleteConditionList extends Fragment {

    private OnCompleteConditionsInteractionListener mListener;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<String> completeConditionList;
    private RecyclerView list;
    private View rootView;
    private ViewGroup container;
    private LayoutInflater inflater;

    public CompleteConditionList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        this.inflater = inflater;
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_complete_symptom_list, container, false);
        list = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
//            previousTitle = actionBar.getTitle().toString();
//            previousSubTitle = actionBar.getSubtitle().toString();
            actionBar.setTitle(R.string.about);
            actionBar.setSubtitle(R.string.prembros);
        }

        new LoadConditions().execute("conditions.txt");

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCompleteConditionsInteractionListener) {
            mListener = (OnCompleteConditionsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPossibleConditionDetailsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class LoadConditions extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> conditionsList = new ArrayList<>();
            try {
                JSONObject conditionsJSON = new JSONObject(JSONReader.read(getContext(), strings[0]));

                Iterator<?> iterator = conditionsJSON.keys();
                while (iterator.hasNext()) {
                    String condition = (String) iterator.next();
                    if (conditionsJSON.get(condition) instanceof JSONArray) {
                        conditionsList.add(condition.replace("_", " "));
                    }
                }
                Collections.sort(conditionsList);
                completeConditionList = conditionsList;
                return conditionsList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }


    }

    interface OnCompleteConditionsInteractionListener {
        void onCompleteConditionsInteraction(String item);
    }
}
